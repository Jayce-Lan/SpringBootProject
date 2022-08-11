package com.example.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.RandomUtil;
import com.example.dto.LoginFormDTO;
import com.example.dto.Result;
import com.example.dto.UserDTO;
import com.example.entity.User;
import com.example.mapper.UserMapper;
import com.example.service.UserService;
import com.example.utils.RedisConstants;
import com.example.utils.RedisUtils;
import com.example.utils.RegexUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service("userService")
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {
    @Resource
    UserMapper userMapper;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    RedisUtils redisUtils;

    /**
     * 验证手机号登陆/注册的方法
     * @param phone
     * @return
     */
    @Override
    public Result sendCode(String phone) {
        // 校验手机号格式是否正确
        if (RegexUtils.isPhoneInvalid(phone)) {
            return Result.fail("手机号传入错误！");
        }
        // 符合，则生成验证码
        String code = RandomUtil.randomNumbers(6);
        // 验证码保存至session，2分钟后过期
        stringRedisTemplate.opsForValue().set(RedisConstants.LOGIN_CODE_KEY + phone, code,
                RedisConstants.LOGIN_CODE_TTL, TimeUnit.MINUTES);
        // 模拟发送短信验证码
        log.info("======== 短信验证码已发送，内容为： {} ========", code);
        return Result.ok();
    }

    @Override
    public Result login(LoginFormDTO loginFormDTO) {
        String phone = loginFormDTO.getPhone();
        // 校验手机号
        if (RegexUtils.isPhoneInvalid(phone)) {
            return Result.fail("手机号传入错误！");
        }
        // 校验缓存中的验证码与所输入是否一致
        String cacheCode = stringRedisTemplate.opsForValue().get(RedisConstants.LOGIN_CODE_KEY + phone);
        String code = loginFormDTO.getCode();
        if (!StringUtils.hasText(cacheCode) || !cacheCode.equals(code)) {
            return Result.fail("验证码错误！");
        }

        // 校验用户是否存在
        User user = userMapper.queryUserInfo(phone);
        if (user == null) {
            user = createNewUser(loginFormDTO);
            userMapper.addUser(user);
        }

        // 将用户信息保存至Redis
        // 随机生成token作为登陆令牌
        String token = UUID.randomUUID().toString();
        // 将user对象转为HashMap存储
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
//        Map<String, Object> userMap = new HashMap<>();
//        userMap.put("id", userDTO.getId().toString());
//        userMap.put("nickName", userDTO.getNickName());
//        userMap.put("icon", userDTO.getIcon());
        // 由于 id中的 Long类型无法进行转换，因此要对map中的类型做转String操作
        Map<String, Object> userMap = BeanUtil.beanToMap(userDTO, new HashMap<>(),
                CopyOptions.create()
                        .setIgnoreNullValue(true)
                        .setFieldValueEditor((key, value) -> value.toString()));

        // 存储
        String tokenKey = RedisConstants.LOGIN_USER_KEY + token;
        stringRedisTemplate.opsForHash().putAll(tokenKey, userMap);
        // 设置token有效期
        stringRedisTemplate.expire(tokenKey, RedisConstants.LOGIN_USER_TTL, TimeUnit.MINUTES);

        return Result.ok(token);
    }

    private User createNewUser(LoginFormDTO loginFormDTO) {
        User user = new User();
        user.setId(Long.valueOf(redisUtils.getIdUtil(RedisConstants.USER_LOGIN_ID)));
        user.setNickName("新用户" + user.getId());
        user.setPhone(loginFormDTO.getPhone());
        user.setPassword(loginFormDTO.getPassword());
        return user;
    }
}
