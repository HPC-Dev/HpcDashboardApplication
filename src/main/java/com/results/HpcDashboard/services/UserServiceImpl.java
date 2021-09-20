package com.results.HpcDashboard.services;

import com.results.HpcDashboard.dto.UserRegistrationDto;
import com.results.HpcDashboard.models.PasswordResetToken;
import com.results.HpcDashboard.models.Role;
import com.results.HpcDashboard.models.User;
import com.results.HpcDashboard.repo.PasswordResetTokenRepository;
import com.results.HpcDashboard.repo.RoleRepository;
import com.results.HpcDashboard.repo.UserRepository;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private JavaMailSender mailSender;


    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }


    public void register(UserRegistrationDto userRegistrationDto, String siteURL)
            throws UnsupportedEncodingException, MessagingException {

        User user = new User();
        user.setFirstName(userRegistrationDto.getFirstName());
        user.setLastName(userRegistrationDto.getLastName());
        user.setUserName(userRegistrationDto.getUserName());
        user.setEmail(userRegistrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));
        user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER"), roleRepository.findByName("ROLE_TEAM")));

        String randomVerificationCode = RandomString.make(64);
        String randomApprovalCode = RandomString.make(56);
        user.setVerificationCode(randomVerificationCode);
        user.setApprovalCode(randomApprovalCode);
        user.setEnabled(false);

        userRepository.save(user);

        sendVerificationEmail(user, siteURL);
        sendApprovalEmail(user, siteURL);
    }

    private void sendVerificationEmail(User user, String siteURL)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = "hpcdashboard@outlook.com";
        String senderName = "HPC Dashboard";
        String subject = "Please verify your registration";
        String content = "<br>Hello [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you<br>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getFirstName());
        String verifyURL = siteURL + "/register/verify?code=" + user.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);

    }


    private void sendApprovalEmail(User user, String siteURL)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = "sai.kovouri@amd.com, ashok.manikonda@amd.com,anre.kashyap@amd.com,kevin.mayo@amd.com";
        //String toAddress = "sai.kovouri@amd.com";
        String fromAddress = "hpcdashboard@outlook.com";
        String senderName = "HPC Dashboard";
        String subject = "Please approve the new user";
        String content =

                "<br>Below are the details of the signed up user <br>"+
                "Name: "+ user.getFullName() + "<br>"
                +
                 "Email: "+user.getEmail() +"<br> <br>"+
                "Please click the link below to approve the user<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">APPROVE</a></h3>";


        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(InternetAddress.parse(toAddress));
        helper.setSubject(subject);

        String verifyURL = siteURL + "/register/approve?code=" + user.getApprovalCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);

    }


    private void sendApprovedEmail(User user, String siteURL)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = "hpcdashboard@outlook.com";
        String senderName = "HPC Dashboard";
        String subject = "Your account is approved";
        String content = "<br>Hello [[name]],<br>"
                + "Please click the link below to login<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">LOGIN</a></h3>"
                + "Thank you<br>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getFirstName());
        String loginURL = siteURL + "/login" ;

        content = content.replace("[[URL]]", loginURL);

        helper.setText(content, true);

        mailSender.send(message);

    }


    public boolean verify(String verificationCode) {
        User user = userRepository.findByVerificationCode(verificationCode);

        if (user == null || user.isEnabled()) {
            return false;
        } else {
            user.setEnabled(true);
            userRepository.save(user);

            return true;
        }

    }

    public int approve(String approvalCode , String siteURL) throws UnsupportedEncodingException, MessagingException {
        User user = userRepository.findByApprovalCode(approvalCode);

        if (user == null ) {
            return 0;
        }else if( user.isApproved()){

            return 1;
        }
        else  {
            user.setApproved(true);
            sendApprovedEmail(user, siteURL);
            userRepository.save(user);
            return 2;
        }

    }


    public User save(UserRegistrationDto register) {
        User user = new User();
//        user.setFirstName(register.getFirstName());
//        user.setLastName(register.getLastName());
//        user.setUserName(register.getUserName());
//        user.setEmail(register.getEmail());
//        user.setPassword(passwordEncoder.encode(register.getPassword()));
//        user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER"), roleRepository.findByName("ROLE_TEAM")));
        return userRepository.save(user);
    }

    public void update(String uname, String roles){
        User  user = userRepository.findByUserName(uname);

        if(roles != null || roles.length() >1)
        {
            String[] s = roles.split(":");
            if(s.length == 1 && s[0].equals("USER")){
                user.setRoles(new ArrayList<>(Arrays.asList(roleRepository.findByName("ROLE_USER"))));
            }
            else if(s.length == 1 && s[0].equals("ADMIN") ){
                user.setRoles(new ArrayList<>(Arrays.asList(roleRepository.findByName("ROLE_ADMIN"))));
            }
            else if(s.length == 1 && s[0].equals("TEAM") ){
                user.setRoles(new ArrayList<>(Arrays.asList(roleRepository.findByName("ROLE_TEAM"))));
            }
            else if(s.length > 1){
                user.setRoles(new ArrayList<>(Arrays.asList(roleRepository.findByName("ROLE_TEAM"), roleRepository.findByName("ROLE_USER"))));
            }

            userRepository.save(user);
        }

    }


    @Override
    public UserDetails loadUserByUsername(String uname) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(uname);
        if (user == null) {
            user = userRepository.findByUserName(uname);
        }
        if (user == null) {
            throw new UsernameNotFoundException("Username is null");
        }
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(), user.isEnabled(),true,true,user.isApproved(),
                mapRolesToAuthorities(user.getRoles()));


//        public User(String username, String password, boolean enabled,
//        boolean accountNonExpired, boolean credentialsNonExpired,
//        boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {

        return userDetails;
    }

    private Collection <? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }


    @Override
    public void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken myToken = new PasswordResetToken(token, user);
        passwordResetTokenRepository.save(myToken);
    }

    @Override
    public PasswordResetToken getPasswordResetToken(final String token) {
        return passwordResetTokenRepository.findByToken(token);
    }

    @Override
    public Optional<User> getUserByPasswordResetToken(final String token) {
        return Optional.ofNullable(passwordResetTokenRepository.findByToken(token) .getUser());
    }

    @Override
    public Optional<User> getUserByID(final long id) {
        return userRepository.findById(id);
    }

    @Override
    public void changeUserPassword(final User user, final String password) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    @Override
    public boolean checkIfValidOldPassword(final User user, final String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

//    public boolean isEnabled() {
//        return user.isEnabled();
//    }



}
