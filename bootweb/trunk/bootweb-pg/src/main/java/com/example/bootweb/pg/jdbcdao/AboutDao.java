package com.example.bootweb.pg.jdbcdao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.bootweb.pg.domain.About;

@Repository
public class AboutDao {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  public About get(Long id) {
    List<About> abouts = jdbcTemplate.query("select * from about where id = ?", //
        new Object[] {
            id }, //
        new BeanPropertyRowMapper<About>(About.class));

    About about = new About();
    if (null != abouts && !abouts.isEmpty()) {
      about = abouts.get(0);
    }
    return about;
  }

  public List<UuidBean> listBeanLikeName(String name) {
    return jdbcTemplate.query("select * from about where name like ?", //
        new Object[] {
            "%" + name + "%" }, //
        new BeanPropertyRowMapper<UuidBean>(UuidBean.class));
  }

}
