package com.example.controller;

import com.example.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@Slf4j
public class WebFluxCRUDController {
    Map<Long, User> userMap = new HashMap<>();

    @PostConstruct // 依赖关系注入完成后，执行初始化
    public void init() {
        userMap.put(Long.valueOf(1), new User(1, "Tony", 21));
        userMap.put(Long.valueOf(2), new User(2, "Jack", 20));
    }

    @GetMapping("/list")
    public Flux<User> getAllUsers() {
        return Flux.fromIterable(userMap.entrySet().stream()
                                    .map(p -> p.getValue())
                                    .collect(Collectors.toList()));
    }

    /**
     * 根据id获取用户
     * @param id 用户id
     * @return 返回用户
     */
    @GetMapping("/getuser/{id}")
    public Mono<User> getUserById(@PathVariable Long id) {
        return Mono.justOrEmpty(userMap.get(id));
    }

    /**
     * 添加用户
     * @param user 被添加用户
     * @return 返回结果
     */
    @PostMapping("/adduser")
    public Mono<ResponseEntity<String>> addUser(User user) {
        userMap.put(user.getId(), user);
        return Mono.just(new ResponseEntity<>("添加成功", HttpStatus.CREATED));
    }

    @PutMapping("/updateuser/{id}")
    public Mono<ResponseEntity<User>> putUser(@PathVariable Long id, User user) {
        log.info(user.toString());
        user.setId(id);
        userMap.put(id, user);
        return Mono.just(new ResponseEntity<>(user, HttpStatus.CREATED));
    }

    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<String>> deleteUser(@PathVariable Long id) {
        userMap.remove(id);
        return Mono.just(new ResponseEntity<>("删除成功", HttpStatus.ACCEPTED));
    }
}
