package com.sayas.filmhub.web.admin;

import com.sayas.filmhub.domain.user.UserService;
import com.sayas.filmhub.domain.user.dto.UserCredentialsDto;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;



@Controller
@RequestMapping("/admin")
public class UsersController {

    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String viewUsers(Model model, @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserCredentialsDto> usersPage = userService.getAllUsers(pageable);

        model.addAttribute("users", usersPage.getContent());
        model.addAttribute("currentPage", usersPage.getNumber());
        model.addAttribute("totalPages", usersPage.getTotalPages());

        return "admin/users";
    }
    @PutMapping("/shadow-ban/{userName}")
    public String shadowBan(@PathVariable String userName) throws NotFoundException {
        userService.shadowBan(userName);
        return "redirect:/admin/view-users";
    }
    @PutMapping("/unban/{userName}")
    public String unBan(@PathVariable String userName) throws NotFoundException {
        userService.unban(userName);
        return "redirect:/admin/view-users";
    }
    @DeleteMapping("/delete/{userName}")
    public String deleteUser(@PathVariable String userName) throws NotFoundException {
        userService.deleteUser(userName);
        return "redirect:/admin/view-users";
    }
}
