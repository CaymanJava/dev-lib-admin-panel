package org.cayman.service;

import lombok.extern.slf4j.Slf4j;
import org.cayman.dto.Category;
import org.cayman.dto.SaveDto;
import org.cayman.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Controller
@Slf4j
public class CategoryService {
    private final Constants constants;
    private final RestTemplate restTemplate;

    @Autowired
    public CategoryService(Constants constants, RestTemplate restTemplate) {
        this.constants = constants;
        this.restTemplate = restTemplate;
    }

    public List<Category> getAllCategories() {
        String url = constants.getBookServiceUrl() + "/category";
        Category[] categories = restTemplate.getForObject(url, Category[].class);
        return Arrays.asList(categories);
    }

    public Category saveCategory(String name) {
        String url = constants.getBookServiceUrl() + "/category/save";
        return restTemplate.postForObject(url, new SaveDto(name), Category.class);
    }

    public Category updateCategory(int id, String name) {
        String url = constants.getBookServiceUrl() + "/category/update";
        return restTemplate.postForObject(url, new Category(id, name), Category.class);
    }

    public boolean deleteCategory(int id) {
        String url = constants.getBookServiceUrl() + "/category/delete?id={id}";
        return restTemplate.getForObject(url, Boolean.class, id);
    }
}
