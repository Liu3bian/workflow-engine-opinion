package com.fdbatt.security;

import com.fdbatt.entity.User;
import com.fdbatt.repository.PermissionMapper;
import com.fdbatt.repository.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserMapper userMapper;
    private final PermissionMapper permissionMapper;

    public UserDetailsServiceImpl(UserMapper userMapper,
                                  PermissionMapper permissionMapper) {
        this.userMapper = userMapper;
        this.permissionMapper = permissionMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        List<String> perms =
                permissionMapper.findPermsByUserId(user.getId());

        return new LoginUser(user, perms);
    }
}
