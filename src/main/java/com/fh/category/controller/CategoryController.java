package com.fh.category.controller;

import com.fh.category.service.CategoryService;
import com.fh.common.Ignore;
import com.fh.common.ServerResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("category")
@Api(value = "分类接口", tags = { "分类接口123" })
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @RequestMapping("queryList")
    @Ignore
    @ApiOperation(value = "查询分类", notes = "query")
    public ServerResponse queryList(){
        return categoryService.queryList();
    }


    @RequestMapping(name = "add",method = RequestMethod.POST)
    @Ignore
    @ApiOperation(value = "增加分类",notes = "add")
    public ServerResponse add(@ApiParam(name = "name", value = "用户名称") @RequestBody String name,
                              @ApiParam(name = "age", value = "用户年龄", required = true) Integer age){
        System.out.println("name="+name+",age="+age);
        return ServerResponse.success();
    }

}
