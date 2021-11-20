package web.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.dao.UserDao;
import web.model.Role;
import web.model.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImp implements UserService {

    private UserDao userDao;

    public UserServiceImp(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    @Transactional
    public void add(User user) {
        userDao.add(user);
    }

    @Override
    @Transactional
    public void update(User user) {
        userDao.update(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getUsers() {
        return userDao.getUsers();
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userDao.deleteUser(id);
    }

    @Override
    @Transactional
    public User getUserById(Long id) {
        return userDao.getUserById(id);
    }

    @Transactional
    public Set<Role> getRoles(Long id) {
        return userDao.getRoles(id);
    }

    @Override
    public void setModified(User user, Date modified) {
        DateFormat df = new SimpleDateFormat("HH:mm:ss dd-MM-YYYY");
        user.setModified(df.format(modified));
    }

    @Override
    public void setCreated(User user, Date created) {
        DateFormat df = new SimpleDateFormat("HH:mm:ss dd-MM-YYYY");
        user.setCreated(df.format(created));
    }

}
