package com.krekerok.blogapp.service.impl;

import com.krekerok.blogapp.dto.requests.AppUserRequestDto;
import com.krekerok.blogapp.entity.RedisUser;
import com.krekerok.blogapp.exception.ActivationCodeNotFoundException;
import com.krekerok.blogapp.exception.FieldExistsException;
import com.krekerok.blogapp.mapper.UserMapper;
import com.krekerok.blogapp.service.AppUserService;
import com.krekerok.blogapp.service.MailSenderService;
import com.krekerok.blogapp.service.RedisService;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisTemplate<String, Object> template;
    @Autowired
    private AppUserService appUserService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MailSenderService mailSenderService;


    @Override
    public String registerUser(AppUserRequestDto appUserRequestDto) {

        appUserService.checkingForExistenceInTheDatabase(appUserRequestDto.getUsername(), appUserRequestDto.getEmail());

        RedisUser redisUser = getRedisUserFromUserCreateDTO(appUserRequestDto);

        redisUserExists(redisUser.getEmail(), redisUser.getUsername());

        saveRedisUser(redisUser);
        sendingAnEmailMessageForEmailVerification(redisUser);
        return redisUser.getActivationCode();
    }

    private RedisUser getRedisUserFromUserCreateDTO(AppUserRequestDto appUserRequestDto) {
        RedisUser redisUser = UserMapper.INSTANCE.toRedisUserWithoutPassword(appUserRequestDto);
        redisUser.setPassword(passwordEncoder.encode(appUserRequestDto.getPassword()));

        return redisUser;
    }

    private void redisUserExists(String email, String username) {
        RedisUser redisUser = (RedisUser) template.opsForValue().get(email);
        String usernameFromRedis = (String) template.opsForValue().get(username);

        if (redisUser != null){
            throw new FieldExistsException("User with this email is already registered");
        } else if (usernameFromRedis != null){
            throw new FieldExistsException("User with this username is already registered");
        }
    }

    private void saveRedisUser(RedisUser redisUser){
        template.opsForValue().set(redisUser.getEmail(), redisUser, 5, TimeUnit.MINUTES);
        template.opsForValue().set(redisUser.getUsername(), redisUser.getUsername(), 5, TimeUnit.MINUTES);
        log.info("User with username - " + redisUser.getUsername()
            + ", and email - " + redisUser.getEmail() + " has been saved to the REDIS database.");
    }

    private void sendingAnEmailMessageForEmailVerification(RedisUser user) {
        String link = String.format("http://localhost:8080/api/v1/registration/verify/%s?email=%s",
            user.getActivationCode(), user.getEmail());

        mailSenderService.send(user.getEmail(), "Account activation", buildEmail(user.getUsername(), link));
    }

    @Transactional
    @Override
    public void verifyUser(String email, String activationCode) {
        RedisUser redisUser = getRedisUserByEmail(email);

        if (redisUser != null && redisUser.getActivationCode().equals(activationCode)){
            deleteRedisUser(email, redisUser.getUsername());
            appUserService.createUser(redisUser);

        } else {
            throw new ActivationCodeNotFoundException("Activation link is outdated");
        }

    }


    private RedisUser getRedisUserByEmail(String email) {
        return (RedisUser) template.opsForValue().get(email);
    }


    private void deleteRedisUser(String email, String username) {
        template.opsForValue().getAndDelete(email);
        template.opsForValue().getAndDelete(username);
    }


    private String buildEmail(String name, String link) {
        return
            "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n"
                +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n"
                +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n"
                +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n"
                +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n"
                +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n"
                +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n"
                +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n"
                +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n"
                +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n"
                +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi "
                + name
                + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\""
                + link
                + "\">Activate Now</a> </p></blockquote>\n Link will expire in 5 minutes. <p>See you soon</p>"
                +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }
}
