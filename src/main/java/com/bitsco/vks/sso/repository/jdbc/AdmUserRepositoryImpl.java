package com.bitsco.vks.sso.repository.jdbc;

import com.bitsco.vks.common.constant.Constant;
import com.bitsco.vks.common.crypt.Base64;
import com.bitsco.vks.common.exception.CommonException;
import com.bitsco.vks.common.response.Response;
import com.bitsco.vks.common.util.Utils;
import com.bitsco.vks.sso.dto.UserSaveDTO;
import com.bitsco.vks.sso.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class AdmUserRepositoryImpl implements AdmUserRepository{

    @Autowired
    JdbcTemplate jdbcTemplate;

    private static MessageDigest md;
    private static String algorithm = "MD5";
    private static String provider = "SUN";
    private static String charset = "UTF-8";
    private static String presalt = "<PWD>";
    private static String sufsalt = "</PWD>";

    @Override
    public List<User> getLst(User req) {
        try {
            RowMapper<User> rm = new SingleColumnRowMapper<User>() {
                @Override
                public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                    User users = new User();
                    users.setUsername(rs.getString("USERID"));
                    return users;
                }
            };

            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                    .withCatalogName(Constant.PACKAGE.PKG_ADM_USERS)
                    .withFunctionName(Constant.FUNCTION.FN_SEARCH)
                    .returningResultSet("return",rm);

            SqlParameterSource param = new MapSqlParameterSource()
                    .addValue("xxx", req.getAddress());
            return jdbcCall.executeFunction((Class<List<User>>) (Class) List.class, param);
        } catch (CommonException e) {
            throw(e);
        }
    }

    @Override
    public String insOrUpd(UserSaveDTO req, String username) {
        try {
            String password = "";
            if ("I".equals(req.getAction())) {
                try {
                    password = encode(Base64.decodeBase64(req.getUser().getPassword()));
                } catch (Exception e) {
                    throw new CommonException(Response.DATA_INVALID, "Không thể giải mã mật khẩu");
                }
            }
            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                    .withCatalogName(Constant.PACKAGE.PKG_ADM_USERS)
                    .withFunctionName(Constant.FUNCTION.FN_INSERT_UPDATE);

            SqlParameterSource param = new MapSqlParameterSource()
                    .addValue("p_action", req.getAction())
                    .addValue("p_userid", req.getUser() != null ? req.getUser().getUsername() : null)
                    .addValue("p_password", password)
                    .addValue("p_sppid", req.getSppid())
                    .addValue("p_inspcode", req.getInspcode())
                    .addValue("p_expiredate", req.getExpiredate())
                    .addValue("p_locked", req.isLocked() ? "Y" : null)
                    .addValue("p_delothers", null)
                    .addValue("p_crtuser", username)
                    .addValue("p_departid", req.getDepartid())
                    .addValue("p_groupid", req.getGroupid());

//            sp_log(req.getAction(), req, username);
            return (String) jdbcCall.execute(param).get("return");
        } catch (CommonException e) {
            throw(e);
        }
    }

    @Override
    public String delete(UserSaveDTO req, String username) {
        try {
            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                    .withCatalogName(Constant.PACKAGE.PKG_ADM_USERS)
                    .withFunctionName(Constant.FUNCTION.FN_DELETE);

            SqlParameterSource param = new MapSqlParameterSource()
                    .addValue("p_userid", req.getUser() != null ? req.getUser().getUsername() : null);

//            sp_log("D", req, username);
            return (String) jdbcCall.execute(param).get("return");
        } catch (CommonException e) {
            throw(e);
        }
    }

    /**
     *
     * @param action LƯU THAO TÁC -- I (INSERT), U (UPDATE), D (DELETE)
     * @param req DỮ LIỆU LƯU LOG
     * @param username TÊN NGƯỜI TAO TÁC
     */
    private void sp_log(String action,  UserSaveDTO req, String username) {
        try {
            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                    .withCatalogName(Constant.PACKAGE.PKG_ADM_USERS)
                    .withFunctionName(Constant.FUNCTION.SP_LOG_DELETE);

//            String ip = getPublicIpByIpify();
//            String ip1 = getClientName();
            SqlParameterSource param = new MapSqlParameterSource()
                    .addValue("p_action", action)
                    .addValue("p_userid", req.getUser() != null ? req.getUser().getUsername() : null)
                    .addValue("p_password", req.getUser() != null ? req.getUser().getPassword() : null)
                    .addValue("p_sppid", req.getSppid())
                    .addValue("p_inspcode", req.getInspcode())
                    .addValue("p_expiredate", Utils.getSqlDate(req.getExpiredate()))
                    .addValue("p_locked", req.isLocked() ? "Y" : null)
                    .addValue("p_delothers", null)
                    .addValue("p_crtuser", username)
                    .addValue("p_departid", req.getDepartid())
                    .addValue("p_groupid", req.getGroupid())
                    .addValue("p_ip", "")
//                    .addValue("p_ip", ip)
                    .addValue("P_IP1", "");
//                    .addValue("P_IP1", ip1);
            jdbcCall.execute(param);
        } catch (CommonException e) {
            throw(e);
        }
    }

    private String getPublicIpByIpify() {
        try (java.util.Scanner s = new java.util.Scanner(new java.net.URL("https://api.ipify.org").openStream(), "UTF-8").useDelimiter("\\A")) {
            System.out.println("My current IP address is " + s.next());
            return s.next();
        } catch (java.io.IOException e) {
            return "";
        }
    }

    private String getClientName() {
        InetAddress IP;
        try {
            IP = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            return "";
        }
        return IP.toString();
    }

    private String encode(String stPassword_) {
        try {
            md = MessageDigest.getInstance(algorithm, provider);

            if (md != null) {
                md.reset();
                return processPassword(stPassword_);
            }
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected static String processPassword(String stPassword_) throws IOException, UnsupportedEncodingException {
        String s = presalt + stPassword_ + sufsalt;
        md.update(s.getBytes(charset));
        byte[] byteData = md.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
