package com.microservices.authenticationserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Repository
public class UserDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public UserModel getUserDetails(String username){
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        String sql = "SELECT * FROM USERS WHERE username=?";
        List<UserModel> list = jdbcTemplate.query(sql, new String[] {username}, (ResultSet rs, int rowNum) -> {
            UserModel userModel = new UserModel();
            userModel.setUsername(username);
            userModel.setPassword(rs.getString("PASSWORD"));

            return userModel;
        });

        if(list != null && list.size()>0){
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_SYSTEMADMIN");
            grantedAuthorities.add(grantedAuthority);
            list.get(0).setGrantedAuthorities(grantedAuthorities);
            return list.get(0);
        }

        return null;
    }
}
