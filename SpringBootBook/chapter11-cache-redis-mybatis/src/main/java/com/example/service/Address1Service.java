package com.example.service;

import com.example.pojo.AddressDTO;

import java.util.List;
import java.util.Map;

public interface Address1Service {
    List<AddressDTO> queryAddressList();
    String queryAddressByAddressNo(String addressNo);
}
