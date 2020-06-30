package com.Dao;

import com.Model.LoginTicket;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface LoginTicketDao {

    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert({"insert into loginticket(userid, ticket, expired, status) values (#{userId},#{ticket},#{expired},#{status})"})
    int insertLoginTicket(LoginTicket loginTicket);

    @Select({"select id, userid, ticket, expired, status from loginticket where id=#{id}"})
    LoginTicket selectById(int id);

    @Select({"select id, userid, ticket, expired, status from loginticket where ticket like #{ticket}"})
    LoginTicket selectByTicket(String ticket);

    @Update({"update loginticket set status = #{status} where ticket = #{ticket}"})
    void updateStatus(@Param("ticket") String ticket, @Param("status") int status);

    @Delete({"delete from loginticket where id=#{id}"})
    void deleteById(int id);
}