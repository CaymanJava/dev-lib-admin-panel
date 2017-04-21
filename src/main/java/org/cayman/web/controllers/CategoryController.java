package org.cayman.web.controllers;

import org.cayman.dto.Category;
import org.cayman.exception.DeleteEntityException;
import org.cayman.exception.SaveEntityException;
import org.cayman.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @RequestMapping(value = "category/add", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addCategory(@RequestParam("name") String name) {
        Category category = categoryService.saveCategory(name);
        if (category == null) throw new SaveEntityException("Category hadn't been saved");
        return "redirect:/books";
    }

    @RequestMapping(value = "category/edit", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editCategory(@RequestParam("id") int id, @RequestParam("name") String name) {
        Category category = categoryService.updateCategory(id, name);
        if (category == null) throw new SaveEntityException("Category hadn't been updated");
        return "redirect:/books";
    }

    @RequestMapping(value = "category/delete", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteCategory(@RequestParam("id") int id) {
        boolean response = categoryService.deleteCategory(id);
        if (!response) throw new DeleteEntityException("Category hadn't been deleted");
        return "redirect:/books";
    }
}
