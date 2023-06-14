package example.shop.demo.controllers;

import example.shop.demo.entities.Category;
import example.shop.demo.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    @GetMapping("/all")
    public String getAllCategories(){
        return "/category/list";
    }
    @PostMapping("/add")
    public String addCategory(@RequestParam String name, RedirectAttributes redirectAttributes){
        Category category = new Category();
        category.setName(name);
        categoryService.addCategory(category);
        redirectAttributes.addFlashAttribute("successMessage", "Category added successfully!");
        return "redirect:/categories/all";
    }
    @PostMapping("/edit")
    public String editCategory(@RequestParam Long editId, @RequestParam String editName, RedirectAttributes redirectAttributes){
        Category category = categoryService.getCategoryById(editId).get();
        category.setName(editName);
        categoryService.addCategory(category);
        redirectAttributes.addFlashAttribute("successMessage", "Category updated successfully!");
        return "redirect:/categories/all";
    }
    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes){
        categoryService.deleteCategoryById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Category deleted successfully!");
        return "redirect:/categories/all";
    }
}
