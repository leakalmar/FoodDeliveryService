package services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.google.gson.JsonSyntaxException;

import beans.Customer;
import beans.Deliverer;
import beans.Manager;
import beans.Order;
import beans.OrderStatus;
import beans.Role;
import beans.User;
import dao.UserDAO;
import dto.ChangePasswordDTO;
import dto.LogInDTO;
import dto.RegistrationDTO;

public class UserService {
	
	private UserDAO userDAO;
	
	public UserService(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	
	public User register(RegistrationDTO registrationForm) throws JsonSyntaxException, IOException {
		User newRegistration = null;
		User registeredUser = userDAO.getByID(registrationForm.getUsername());
		if( registeredUser == null) {
			switch(registrationForm.getRole()) {
			case Customer:
				newRegistration = new Customer(registrationForm.getUsername(), registrationForm.getPassword(),
						registrationForm.getName(), registrationForm.getSurname(), registrationForm.getGender(),
						registrationForm.getBirthdate(), registrationForm.getRole());
				break;
			case Manager:
				newRegistration = new Manager(registrationForm.getUsername(), registrationForm.getPassword(),
						registrationForm.getName(), registrationForm.getSurname(), registrationForm.getGender(),
						registrationForm.getBirthdate(), registrationForm.getRole());
				break;
			case Deliverer:
				newRegistration = new Deliverer(registrationForm.getUsername(), registrationForm.getPassword(),
						registrationForm.getName(), registrationForm.getSurname(), registrationForm.getGender(),
						registrationForm.getBirthdate(), registrationForm.getRole());
				break;
			default:
				break;
			}
			userDAO.save(newRegistration);
		}
		return newRegistration;
	}

	public User logIn(LogInDTO logInForm) throws JsonSyntaxException, IOException{
		User registeredUser = userDAO.getByID(logInForm.getUsername());
		if(registeredUser == null || !registeredUser.getPassword().equals(logInForm.getPassword())){
			registeredUser = null;
		}
		return registeredUser;
	}

	public void editProfile(User editedUser) throws JsonSyntaxException, IOException {
		userDAO.update(editedUser);
	}

	public User changePassword(User user, ChangePasswordDTO changePasswordForm) throws JsonSyntaxException, IOException {
		User loggedUser = null;
		if(user.getPassword().equals(changePasswordForm.getOldPassword())){
			user.setPassword(changePasswordForm.getNewPassword());
			userDAO.update(user);
			loggedUser = user;
		}
		return loggedUser;
	}

    public List<User> getAll() throws JsonSyntaxException, IOException{
        return (List<User>) userDAO.getAll();
    }

	public void deleteUser(String username) throws JsonSyntaxException, IOException {
		userDAO.delete(userDAO.getByID(username));
	}

	public List<User> getSuspiciousUsers() throws JsonSyntaxException, IOException {
		List<User> suspiciouUsers = new ArrayList<User>();
		Calendar c = Calendar.getInstance();
		c.setTime(new java.util.Date());
		c.add(Calendar.DATE, -31);
		Date lastMonth = (Date) c.getTime();

		for(User user: userDAO.getAll() ){
			if(user.getRole() == Role.Customer){
				int numberCanceledOrders = 0;
				for(Order order: ((Customer)user).getAllOrders()){
					if(order.getStatus() == OrderStatus.Cancelled && order.getOrderDate().after(lastMonth)){
						numberCanceledOrders++;
					}
				}
				if(numberCanceledOrders > 5){
					suspiciouUsers.add(user);
				}
			}
		}
		return suspiciouUsers;
	}

}
