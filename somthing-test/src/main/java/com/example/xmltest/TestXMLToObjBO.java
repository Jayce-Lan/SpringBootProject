package com.example.xmltest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.math.BigDecimal;

@JacksonXmlRootElement(localName = "HEAD")
@Data
public class TestXMLToObjBO {
    @JsonProperty("PSN_NO")
    private String psnNo;
    @JsonProperty("AGE")
    private BigDecimal age;
}
