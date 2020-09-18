package podcast.controller;



import java.sql.Date;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import podcast.model.dao.ActivityDAO;
import podcast.model.dao.OrderTicketDAO;
import podcast.model.dao.ShoppingCart;
//import podcast.model.idao.OrderService;
import podcast.model.javabean.ActivityBean;
import podcast.model.javabean.MemberBean;
//import podcast.model.javabean.OrderBean;
import podcast.model.javabean.OrderItemBean;
import podcast.model.javabean.OrderTicketBean;



@Controller
@SessionAttributes({ "LoginOK", "products_DPP", "ShoppingCart"})
public class ShoppingCartController {

	private final static String SHOW_CART_CONTENT = "Activity/ShowCartContent";
	
	@Autowired
	ServletContext context;
	
//	@Autowired
//	OrderService orderService;

	@GetMapping("ShoppingCart")
    public String shoppingCartPage() throws Exception {
		return "Activity/ShowCartContent";
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(path = "/addTickets")
    public String processAction2(@RequestParam("qty") Integer quantity,
    		@RequestParam("activityName") String activityName,
    		@RequestParam("activityPrice") Integer activityPrice,
    		@RequestParam("activityId") Integer activityId,
    		@RequestParam("activityLocation") String activityLocation,
    		@RequestParam("activityDate") Date activityDate,
    		Model m,HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		m.addAttribute("quantity", quantity);
		m.addAttribute("activityName", activityName);
		m.addAttribute("activityPrice", activityPrice);
		
		//start
		
		//票券按加入購物車,先確認有無登入(取得LoginOK即有)
		MemberBean memberBean = (MemberBean) m.getAttribute("LoginOK");
		if (memberBean == null) {
			System.out.println("No LoginOK");
			return "redirect:/login";
		}		
		System.out.println("GET LoginOK");
		
		HttpSession session = request.getSession(false); 
		if (session == null) {
			return "redirect:/login";
		}
		System.out.println("GET session products_DPP");
		
		ShoppingCart cart = (ShoppingCart) m.getAttribute("ShoppingCart");
		// 如果找不到ShoppingCart物件
		if (cart == null) {
			// 就新建ShoppingCart物件
			cart = new ShoppingCart();
			// 並將此新建ShoppingCart的物件放到session物件內，成為它的屬性物件
			m.addAttribute("ShoppingCart", cart);   
		}
		
		//取得map物件products_DPP,內含資料庫裡所有活動
		Map<Integer, ActivityBean> map = (Map<Integer, ActivityBean>) m.getAttribute("products_DPP");
		System.out.println("map="+map);
		ActivityBean bean = map.get(activityId);
		System.out.println("bean : "+bean.getActivityName());
		
		OrderItemBean oib = new  OrderItemBean(activityId,activityName,
				activityPrice,quantity,activityDate,activityLocation);
		System.out.println("你點選到的票券資訊:"+activityId+activityName+activityPrice+quantity+activityDate+activityLocation);	
		
		// 將OrderItem物件加入ShoppingCart
		cart.addToCart(activityId, oib);
		
		return "redirect:/a#events";
		
	}
	
	
	@PostMapping("UpdateItem.do")
	protected String UpdateItem(
			@RequestParam("cmd")   String cmd,  
			@RequestParam(value = "activityId", required = false) Integer  activityId, 
			@RequestParam(value = "newQty", required = false) Integer  newQty, 
			Model model, 
			RedirectAttributes ra, 
			HttpSession session, SessionStatus status
			) {
		ShoppingCart sc = (ShoppingCart) model.getAttribute("ShoppingCart");
		if (sc == null) {
			status.setComplete();
			return "redirect:/login";
	
		}
		MemberBean memberBean = (MemberBean) model.getAttribute("LoginOK");
		if (memberBean == null) {
			status.setComplete();
			return "redirect:/login";

		}
		if (cmd.equalsIgnoreCase("DEL")) {
	        sc.delete(activityId); // 刪除購物車內的某項商品
		    return SHOW_CART_CONTENT;
		} else if (cmd.equalsIgnoreCase("MOD")) {
			sc.modifyQty(activityId, newQty);   // 修改某項商品的數項
		    return SHOW_CART_CONTENT;
		} else {
			return SHOW_CART_CONTENT;
		}

	}
	
	
	@GetMapping("checkout")
	protected String checkout(Model model, SessionStatus status) {
		MemberBean memberBean = (MemberBean) model.getAttribute("LoginOK");
		if (memberBean == null) {
			status.setComplete();
			return "redirect:/login";
		}
		return "Orders/OrderConfirm";
	}

	//按下再次確認鍵
	@PostMapping("ProcessOrder")
	protected String ShowOrders(
			Model model,
			@RequestParam("ShippingAddress") String ShippingAddress,
			@RequestParam("BNO") String BNO,
			@RequestParam("InvoiceTitle") String InvoiceTitle,
			WebRequest webRequest, SessionStatus status,
			HttpServletRequest request
			) 
	{
		System.out.println("訂購表單: "+ShippingAddress+BNO+InvoiceTitle);
		
		MemberBean memberBean = (MemberBean) model.getAttribute("LoginOK");
		if (memberBean == null) {
			return "redirect:/login";
		
		}
		
		ShoppingCart sc = (ShoppingCart) model.getAttribute("ShoppingCart");
		if (sc == null) {
			// 處理訂單時如果找不到購物車,導向首頁
			return "redirect:/login";

		}
		// 如果使用者取消訂單
		System.out.println("測試零");
		Integer memberId = memberBean.getMemberId();  
		double totalAmount = sc.getSubtotal(); // 取出會員代號
		OrderTicketBean ob2 = new OrderTicketBean(memberId, totalAmount, ShippingAddress, BNO, InvoiceTitle);
		
		//======
		
		Map<Integer, OrderItemBean> content = sc.getContent();
		Set<OrderItemBean> items = new LinkedHashSet<>();
		Set<Integer> set = content.keySet();
		for(Integer i : set) {
			OrderItemBean oib = content.get(i);
			oib.setOrderTicketBean(ob2);
			items.add(oib);
		}
		ob2.setItems(items);
		
		//======
		ServletContext app = request.getServletContext();
    	WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(app);			
		OrderTicketDAO ot = (OrderTicketDAO)context.getBean("OrderTicketDAO");
		ot.insert(ob2);
		
		//status.setComplete();		
		webRequest.removeAttribute("ShoppingCart", WebRequest.SCOPE_SESSION);
		System.out.println("Order Process OK");
		return "redirect:/orderList";
	}	
	
	//按下取消購物
	@GetMapping("abort")
	protected String abort(HttpSession session, Model model, WebRequest webRequest, SessionStatus status)  {
		webRequest.removeAttribute("ShoppingCart", WebRequest.SCOPE_SESSION);
		//沒有移除???
		//status.setComplete();    // 移除所有被@SessionAttributes({"ShoppingCart"})標示的物件
		return  "redirect:/a";
	}
	
	//按下取消訂單
	@GetMapping("cancelOrder")
	protected String cancelOrder(Model model, 
			WebRequest webRequest, SessionStatus status
			) {
		status.setComplete();
//		webRequest.removeAttribute("ShoppingCart", WebRequest.SCOPE_SESSION);
		return  "redirect:/a";
	}
	
}