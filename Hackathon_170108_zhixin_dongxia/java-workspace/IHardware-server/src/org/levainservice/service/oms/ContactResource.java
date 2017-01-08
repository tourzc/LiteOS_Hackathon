package org.levainservice.service.oms;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.levainservice.rest.ResponseUtil;
import org.levainservice.service.AbstractResource;
import org.levainservice.service.oms.dao.IContactDAO;
import org.levainservice.service.oms.dao.impl.ContactImpl;
import org.levainservice.service.oms.model.Contact;

@Path("contactService")
public class ContactResource extends AbstractResource {
	Logger log = Logger.getLogger(ContactResource.class);

	IContactDAO contactDao = new ContactImpl();

	/**
	 * 注册用户
	 * 
	 * @param json
	 *            contact json字符串
	 * @return
	 */
	@POST
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public String insert(String json) {
		log.trace("POST");
		log.info("sign in:" + json);
		try {
			Contact contact = this.getGson().fromJson(json, Contact.class);

			boolean result = contactDao.insert(contact);
			if (result) {
				log.trace("contact is Inserted ...  ok!");

				return ResponseUtil.getSuccessResponse();
			} else {
				log.trace("contact is Inserted ...  error!");

				return ResponseUtil.getFailResponse();
			}
		} catch (Exception e) {
			return ResponseUtil.getResponse(
					ResponseUtil.METHOD_INVOKE_RESULT_FAIL,
					e.getLocalizedMessage());
		}
	}

	/**
	 * 获取联系人列表
	 * 
	 * @param deviceSerialNo
	 *            用户编号
	 * @return
	 */
	@GET
	@Path("/{deviceSerialNo}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getContacts(@PathParam("deviceSerialNo") String deviceSerialNo) {
		log.trace("GET");
		log.info("get contact list:" + deviceSerialNo);
		try {
			List<Contact> contacts = contactDao
					.getContactsBySerialNo(deviceSerialNo);

			String json = this.getGson().toJson(contacts);

			return ResponseUtil.getResponse(
					ResponseUtil.METHOD_INVOKE_RESULT_SUCCESS, json);
		} catch (Exception e) {
			return ResponseUtil.getResponse(
					ResponseUtil.METHOD_INVOKE_RESULT_FAIL,
					e.getLocalizedMessage());
		}
	}

}
