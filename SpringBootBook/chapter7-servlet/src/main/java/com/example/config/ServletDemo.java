package com.example.config;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/servletDemo/*")
@Slf4j
public class ServletDemo extends HttpServlet {
    /**
     * 重写 doGet 方法
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doGet(req, resp);
        log.info("=========== doGet =========");
        // 重写返回值
        resp.getWriter().print("====== ServletDemo ====== ");
    }
}
