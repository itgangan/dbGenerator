package com.itgangan.servcie;

import com.google.gson.Gson;
import com.itgangan.SpringbootdemoApplication;
import com.itgangan.entity.Departmentinfo;
import com.itgangan.mapper.DepartmentinfoMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringbootdemoApplication.class)
public class DepartmentInfoService {

    @Autowired
    DepartmentinfoMapper departmentinfoMapper;

    @Test
    public void test(){

        List<Departmentinfo> list = departmentinfoMapper.selectAll();

        System.out.println(list.size());

        Departmentinfo info = new Departmentinfo();
        info.setCode("01");
        List<Departmentinfo> departmentinfo = departmentinfoMapper.select(info);

        System.out.println(new Gson().toJson(departmentinfo));

        info = departmentinfoMapper.selectByPrimaryKey("01");

        System.out.println(new Gson().toJson(info));

    }
}
