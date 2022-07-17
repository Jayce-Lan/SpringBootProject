package com.example.dao;

import com.example.pojo.AddressDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Address1DAO {
    List<AddressDTO> queryAddressList();
    AddressDTO queryAddressByAddressNo(String addressNo);
}
