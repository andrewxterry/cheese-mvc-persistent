package org.launchcode.controllers;

import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.launchcode.models.forms.AddMenuItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Controller
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private MenuDao menuDao;

    @Autowired
    private CheeseDao cheeseDao;

    @RequestMapping(value="")
    public String index(Model model) {
        model.addAttribute("menus", menuDao.findAll());
        model.addAttribute("title", "Cheese Menu");

        return("/menu/index");
    }
    @RequestMapping(value="add", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute("title", "Add Menu");
        model.addAttribute(new Menu());
        return("/menu/add");
    }
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddForm(@ModelAttribute @Valid Menu newMenu, Model model, Errors errors){
        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Menu");
            return"/menu/add";
        }
        menuDao.save(newMenu);
        return ("redirect:view/" + newMenu.getId());
}
    @RequestMapping(value = "view/{menuId}", method = RequestMethod.GET )
    public String viewMenu(Model model, @PathVariable int menuId){

        Menu displayMenu = menuDao.findOne(menuId);
        model.addAttribute("title", displayMenu.getName());
        model.addAttribute("cheeses", displayMenu.getCheeses());
        model.addAttribute("menuId", displayMenu.getId());

        return("menu/view");
    }
    @RequestMapping(value="add-item/{menuId}", method = RequestMethod.GET)
    public String addItem (Model model, @PathVariable int menuId) {
        Menu menu = menuDao.findOne(menuId);

        AddMenuItemForm form = new AddMenuItemForm(cheeseDao.findAll(), menu);
        model.addAttribute("form", form);
        model.addAttribute("title", "Add item to menu: " + menu.getName());

        return("menu/add-item");
    }
    @RequestMapping(value="add-item", method = RequestMethod.POST)
    public String addItem(Model model, @ModelAttribute @Valid AddMenuItemForm form, Errors errors){
        if(errors.hasErrors()){
            model.addAttribute("form", form);
            return "menu/add-item";
        }
        Cheese theCheese = cheeseDao.findOne(form.getCheeseId());
        Menu theMenu = menuDao.findOne(form.getMenuId());
        theMenu.addItem(theCheese);
        menuDao.save(theMenu);

        return"redirect:/menu/view/" + theMenu.getId();
    }
}
