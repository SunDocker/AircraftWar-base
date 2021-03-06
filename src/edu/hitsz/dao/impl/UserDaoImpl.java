package edu.hitsz.dao.impl;

import edu.hitsz.dao.UserDao;
import edu.hitsz.dao.pojo.User;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * @author SunDocker
 */
public class UserDaoImpl implements UserDao {
    @Override
    public List<User> selectUsersOrderByScoreDesc(int diffNum) {
        File file = new File("userObjects");
        List<User> userList = new ArrayList<>();
        for (File userObj : file.listFiles()) {
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(new FileInputStream(userObj));
                User user = (User) ois.readObject();
                if (user.getDifficulty() != diffNum) {
                    continue;
                }
                for (int i = 0; i <= userList.size(); i++) {
                    if (i == userList.size()) {
                        userList.add(i, user);
                        break;
                    }
                    User cur = userList.get(i);
                    if (user.getScore() >= cur.getScore()) {
                        userList.add(i, user);
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (ois != null) {
                    try {
                        ois.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return userList;
    }

    @Override
    public void saveUser(User user) {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream("userObjects/" + user.getCreateTime()));
            oos.writeObject(user);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void deleteUser(Long createTime) {
        File file = new File("userObjects");
        File deleteFile = null;
        for (File userObj : file.listFiles()) {
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(new FileInputStream(userObj));
                User user = (User) ois.readObject();
                if (Objects.equals(user.getCreateTime(), createTime)) {
                    System.out.println(user.getCreateTime());
                    deleteFile = userObj;
                    break;
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (ois != null) {
                    try {
                        ois.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (deleteFile != null) {
            System.out.println(deleteFile.delete());
        }
    }
}
