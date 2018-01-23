package com.example.bootweb.accessory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.example.bootweb.accessory.dao.case2.User;
import com.example.bootweb.accessory.dao.case2.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class SpringBootJdbcDemoApplicationTests {

  Logger logger = LoggerFactory.getLogger(SpringBootJdbcDemoApplicationTests.class);

  @Autowired
  private UserRepository userRepository;

  @Test
  public void testAll() {
    findAllUsers();
    findUserById();
    createUser();
  }

  @Test
  public void findAllUsers() {
    List<User> users = userRepository.findAll();
    assertNotNull(users);
    assertTrue(!users.isEmpty());

  }

  @Test
  public void findUserById() {
    User user = userRepository.findUserById(1);
    assertNotNull(user);
  }

  private void updateById(Integer id) {
    User newUser = new User(id, "OK", "1234567890@qq.com");
    userRepository.update(newUser);
    User newUser2 = userRepository.findUserById(newUser.getId());
    assertEquals(newUser.getName(), newUser2.getName());
    assertEquals(newUser.getEmail(), newUser2.getEmail());
  }

  @Test
  public void createUser() {
    User user = new User(0, "PK", "pk@gmail.com");
    User savedUser = userRepository.create(user);
    logger.debug("{}", savedUser);
    User newUser = userRepository.findUserById(savedUser.getId());
    assertEquals("PK", newUser.getName());
    assertEquals("pk@gmail.com", newUser.getEmail());
    updateById(newUser.getId());
    userRepository.delete(newUser.getId());
  }
}
