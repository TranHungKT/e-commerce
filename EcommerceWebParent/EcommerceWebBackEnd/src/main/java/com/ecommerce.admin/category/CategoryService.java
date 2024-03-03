package com.ecommerce.admin.category;

import com.ecommerce.common.entity.Category;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.thymeleaf.expression.Strings;


import java.util.*;


@Service
@Transactional
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    private static final int CATEGORY_PER_PAGE = 4;

    public List<Category> listAll(String sortDir) {
        Sort sort = Sort.by("name");
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        List<Category> rootCategories = categoryRepository.findRootCategories(sort);
        return listHierarchicalCategories(rootCategories, sortDir);
    }

    public List<Category> listByPage(CategoryPageInfo categoryPageInfo, int pageNum, String sortDir, String keyword) {
        Sort sort = Sort.by("name");
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNum - 1, CATEGORY_PER_PAGE, sort);

        if (isEmptyString(keyword)) {
            return findCategoryWithPagination(pageable, categoryPageInfo, sortDir);
        }
        return searchCategories(keyword, pageable, categoryPageInfo);
    }

    private void setPageInfo(CategoryPageInfo categoryPageInfo, Page<Category> pageCategories) {
        categoryPageInfo.setTotalPages(pageCategories.getTotalPages());
        categoryPageInfo.setTotalElements(pageCategories.getTotalElements());
    }

    private List<Category> searchCategories(String keyword, Pageable pageable, CategoryPageInfo categoryPageInfo) {
        Page<Category> pageCategories = categoryRepository.search(keyword, pageable);
        setPageInfo(categoryPageInfo, pageCategories);
        List<Category> searchResult = pageCategories.getContent();
        for (Category category : searchResult) {
            category.setHasChildren(!category.getChildren().isEmpty());
        }

        return searchResult;
    }

    private List<Category> findCategoryWithPagination(Pageable pageable, CategoryPageInfo categoryPageInfo, String sortDir) {
        Page<Category> pageCategories = categoryRepository.findRootCategories(pageable);
        setPageInfo(categoryPageInfo, pageCategories);
        List<Category> rootCategories = pageCategories.getContent();

        return listHierarchicalCategories(rootCategories, sortDir);
    }

    private Boolean isEmptyString(String key) {
        return key == null || key.isEmpty();
    }

    public Category get(Integer id) throws CategoryNotFoundException {
        return categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException("Category not found"));
    }

    private List<Category> listHierarchicalCategories(List<Category> rootCategories, String sortDir) {
        List<Category> hierarchicalCategories = new ArrayList<>();

        for (Category rootCategory : rootCategories) {
            hierarchicalCategories.add(Category.copyFull(rootCategory));

            Set<Category> children = sortSubCategories(rootCategory.getChildren(), sortDir);

            for (Category subCategory : children) {
                String name = "--" + subCategory.getName();
                hierarchicalCategories.add(Category.copyFull(subCategory, name));
                listSubHierarchicalCategories(hierarchicalCategories, subCategory, 1);
            }
        }
        return hierarchicalCategories;
    }

    private void listSubHierarchicalCategories(List<Category> hierarchicalCategories,
                                               Category parent, int subLevel) {
        Set<Category> children = sortSubCategories(parent.getChildren());
        int newSubLevel = subLevel + 1;

        for (Category subCategory : children) {
            String name = "";
            for (int i = 0; i < newSubLevel; i++) {
                name += "--";
            }
            name += subCategory.getName();

            hierarchicalCategories.add(Category.copyFull(subCategory, name));

            listSubHierarchicalCategories(hierarchicalCategories, subCategory, newSubLevel);
        }

    }

    public List<Category> listCategoriesUsedInForm() {
        List<Category> categoriesUsedInForm = new ArrayList<>();

        Iterable<Category> categoriesInDB = categoryRepository.findAll();

        for (Category category : categoriesInDB) {
            if (category.getParent() == null) {
                categoriesUsedInForm.add(Category.copyIdAndName(category));

                Set<Category> children = sortSubCategories(category.getChildren());

                for (Category subCategory : children) {
                    String name = "--" + subCategory.getName();
                    categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));

                    listSubCategoryUsedInForm(categoriesUsedInForm, subCategory, 1);
                }
            }
        }

        return categoriesUsedInForm;
    }

    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public void updateCategoryEnabledStatus(Integer id, Boolean enabled) {
        categoryRepository.updateEnabledStatus(id, enabled);
    }

    public void deleteCategory(Integer id) throws CategoryNotFoundException {
        categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        categoryRepository.deleteById(id);
    }


    private void listSubCategoryUsedInForm(List<Category> categoriesUsedInForm, Category parent, int subLevel) {
        int newSubLevel = subLevel + 1;
        Set<Category> children = sortSubCategories(parent.getChildren());

        for (Category subCategory : children) {
            String name = "";
            for (int i = 0; i < newSubLevel; i++) {
                name += "--";
            }
            name += subCategory.getName();

            categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), name));

            listSubCategoryUsedInForm(categoriesUsedInForm, subCategory, newSubLevel);

        }
    }

    public String checkUnique(Integer id, String name, String alias) {
        Category categoryByName = categoryRepository.findCategoryByName(name);
        Category categoryByAlias = categoryRepository.findCategoryByAlias(alias);

        if (categoryByName != null && !Objects.equals(categoryByName.getId(), id)) {
            return "DuplicateName";
        }

        if (categoryByAlias != null && !Objects.equals(categoryByAlias.getId(), id)) {
            return "DuplicateAlias";
        }

        return "OK";
    }

    private SortedSet<Category> sortSubCategories(Set<Category> children) {
        return sortSubCategories(children, "asc");
    }

    private SortedSet<Category> sortSubCategories(Set<Category> children, String sortDir) {
        SortedSet<Category> sortedChildren = new TreeSet<>(new Comparator<Category>() {
            @Override
            public int compare(Category cat1, Category cat2) {
                if (sortDir.equals("asc")) {
                    return cat1.getName().compareTo(cat2.getName());
                } else {
                    return cat2.getName().compareTo(cat1.getName());
                }
            }
        });

        sortedChildren.addAll(children);

        return sortedChildren;
    }
}
