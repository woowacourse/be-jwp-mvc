package di.stage3.context;

import di.User;

class UserService {

    private UserDao userDao;

    public UserService(final UserDao userDao) {
        this.userDao = userDao;
    }

    private UserService() {
    }

    public User join(final User user) {
        userDao.insert(user);
        return userDao.findById(user.getId());
    }
}
