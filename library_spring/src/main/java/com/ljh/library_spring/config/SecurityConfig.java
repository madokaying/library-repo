package com.ljh.library_spring.config;

import com.ljh.library_spring.filter.JwtAuthenticationTokenFilter;
import com.ljh.library_spring.handler.AccessDeniedHandlerImpl;
import com.ljh.library_spring.handler.AuthenticationEntryPointImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)//全局开启注解鉴权
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private AccessDeniedHandlerImpl accessDeniedHandler;
    @Resource
    private AuthenticationEntryPointImpl authenticationEntryPoint;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Resource
    JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //关闭csrf
                .csrf().disable()
                //不通过Session获取SecurityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //用and()重新返回http的设置
                .and()
                .authorizeRequests()
                /** 对于登录接口 anonymous允许匿名访问，但认证之后不允许访问
                 * permitAll是无论登录不登陆都能访问
                 * 。anyRequest().authenticated()是其它请求任意认证用户均可访问
                 */
                .antMatchers("/book/getBooksList").permitAll()
                .antMatchers("/book/getSearchContent").permitAll()
                .antMatchers("/book/getBookListByTag").permitAll()
                .antMatchers("/book/getBookRankingList").permitAll()
                .antMatchers("/user/judgeUsernameExisted").permitAll()
                .antMatchers("/book/getTagList").permitAll()
                .antMatchers("/user/register").permitAll()
                .antMatchers("/static/**").permitAll()
                .antMatchers("/user/login").anonymous()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated();

        /*把token校验过滤器添加到过滤器链中，且将jwtAuthenticationTokenFilter
        放到UsernamePasswordAuthenticationFilter之前，实现先验证有无token，无token再放行到下一个过滤器进行用户名密码的校验
        有token则直接解析token*/
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);


        //配置异常处理器
        http.exceptionHandling()
                //配置认证失败处理器
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);

        //允许跨域
        http.cors();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
