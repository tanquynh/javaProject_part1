package ra.service;

import ra.constant.Contant;
import ra.model.User;
import ra.repository.FileRepo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static ra.constant.Contant.Status.ACTIVE;
import static ra.constant.Contant.Status.INACTIVE;


public class UserService implements IShop<User> {
    FileRepo<User, Integer> userFileRepo;

    public UserService() {
        this.userFileRepo = new FileRepo<>(Contant.FilePath.USER_FILE);
    }

    @Override
    public void save(User user) {
        userFileRepo.save(user);
    }

    @Override
    public List<User> findAll() {
        return userFileRepo.findAll();
    }

    @Override
    public User findById(int id) {
        return userFileRepo.findById(id);
    }

    @Override
    public int findIndex(int id) {
        return userFileRepo.findByIndex(id);
    }

    @Override
    public int autoInc() {
        return userFileRepo.autoInc();
    }

    public User login(String userName, String password) {
        User user = getUserByUsername(userName);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public User getUserByUsername(String userName) {
        List<User> users = findAll();
        for (User user : users) {
            if (user.getUsername() != null && user.getUsername().equals(userName)) {
                return user;
            }
        }
        return null;
    }

    public void setStatusLogin(String username, boolean newStatus) {
        List<User> users = findAll();
        boolean foundUser = false;

        for (User user : users) {
            if (user.getUsername().equals(username)) {
                user.setStatus(newStatus);
                save(user);
                foundUser = true;
            }else {
                user.setStatus(INACTIVE);
            }
        }

        if (!foundUser) {
            System.err.println("Không tìm thấy người dùng có tên đăng nhập: " + username);
        }
    }

    public List<User> getSortUsersList() {
        List<User> sortsUsers = findAll();
        Collections.sort(sortsUsers, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o1.getUsername().compareTo(o2.getUsername());
            }
        });
        return sortsUsers;
    }

    public List<User> getFitterUsers( String username) {
        List<User> users = findAll();
        List<User> fitterUser = new ArrayList<>();
        for (User user: users) {
            if(user.getUsername().toLowerCase().contains(username.toLowerCase())) {
                fitterUser.add(user);
            }
        } return fitterUser;
    }
    public User getUserByUsename(String userName) {
       List<User> users = findAll();
        for (User user : users) {
            if (user.getUsername() != null && user.getUsername().equals(userName)) {
                return user;
            }
        }
        return null;
    }

    public void updateImportance(boolean status, String username) {
        List<User>users = findAll();
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                user.setImportance(status);
                save(user);
                break;
            }
        }
    }

    public User userActive() {
        List<User> users = findAll();
        for (User user : users) {
            if (user != null && user.isStatus()) {
                return user;
            }
        }
        return null;
    }

}

