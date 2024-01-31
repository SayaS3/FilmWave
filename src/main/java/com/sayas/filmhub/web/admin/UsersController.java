package com.sayas.filmhub.web.admin;

import com.sayas.filmhub.domain.user.UserService;
import com.sayas.filmhub.domain.user.dto.UserCredentialsDto;
import javassist.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

@Controller
public class UsersController {

    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin/view-users")
    public String viewUsers(Model model) {
        List<UserCredentialsDto> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/users";
    }
    @PutMapping("/admin/shadow-ban/{userName}")
    public String shadowBan(@PathVariable String userName) throws NotFoundException {
        userService.shadowBan(userName);
        return "redirect:/admin/view-users";
    }
    @PutMapping("/admin/unban/{userName}")
    public String unBan(@PathVariable String userName) throws NotFoundException {
        userService.unban(userName);
        return "redirect:/admin/view-users";
    }
    @DeleteMapping("/admin/delete/{userName}")
    public String deleteUser(@PathVariable String userName) throws NotFoundException {
        userService.deleteUser(userName);
        return "redirect:/admin/view-users";
    }
}
