# InquiryBox

****

链接<http://39.97.168.17:8080/quiryBox/SXCSXCSS/1

****

##   登录注册

#### 前情提要

本人的QQ邮箱阿里云25端口审核没过，切勿发送邮箱，其中包括注册和登录，望海涵。

账号：401648921@qq.com

密码：zhen415322

这是管理员账号，请直接登录，打开注册界面请不要提交，望海涵。

###      邮箱和用户名都能登录

~~~java
  User user;
        if(userLogintext.contains("@")){
            user = userMapper.selectByEmail(userLogintext);
        }else{
            user = userMapper.selectByUserName(userLogintext);
        }
~~~

###            检查用户输入合法性

~~~javascript
var flag = true;
	$("#userRegister").click(function () {
		flag = true;
		if(userName1.val().length>10||userName1.val().length<6||testName.test(userName1.val())==false){
			$("#testUser").text("用户名错误");
			return false;
		}
		if(testPassword.test(userPassword.val())==true){
			$("#testUser").text("密码不能是纯数字");
			return false;
		}
		if(userPassword.val().length>12||userPassword.val().length<6){
			$("#testUser").text("密码长度错误");
			return false;
		}
		if(userPassword.val()!=userRePassword.val()){
			$("#testUser").text("确认密码不一致");
			return false;
		}
		if(testEmail.test(userEmail.val())==false){
			$("#testUser").text("邮箱格式错误");
			return false;
		}
		$.ajax({
			type:"post",
			url:"/testUserName",
			data:{userName:userName1.val()},
			dataType:'json',
			success:function (data) {
				if(data['result']!= "succeed"){
					flag = false;
					window.alert("用户名重复");
					return false;
				}
			},
			beforeSend: function(request) {
				request.setRequestHeader(csrgHead, csrf);
			},
			error:function () {
				$("#testUser").text("服务器超时");
			}
		})
		$.ajax({
			type:"post",
			url:"/testEmail",
			data:{userEmail:userEmail.val()},
			dataType:'json',
			success:function (data) {
				if(data['result']!= "succeed"){
					flag = false;
					window.alert("邮箱重复");
					return false;
				}
			},
			beforeSend: function(request) {
				request.setRequestHeader(csrgHead, csrf);
			},
			error:function () {
				$("#testUser").text("服务器超时");
			}
		})
		$.ajax({
			type:"post",
			url:"/testCode",
			data:{verifyCodeActual:userTest.val()},
			dataType:'json',
			success:function (data) {
				if(data['result']!= "succeed"){
					flag = false;
					window.alert("验证码错误");
					return false;
				}else{
					userPassword.val(hex_md5(userPassword.val()));
					if(flag == true){
						$("#ajaxForm").submit();
					}
				}
			},
			beforeSend: function(request) {
				request.setRequestHeader(csrgHead, csrf);
			},
			error:function () {
				window.alert("服务器超时");
			}
		})
~~~



## 用户修改用户信息

####      修改邮箱

~~~html
   <div class="accordion-group">
     <div class="accordion-heading">
         <a  class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseEmail">
                                修改邮箱
           </a>
      </div>
      <div id="collapseEmail" class="accordion-body collapse" style="height: 0px; ">
          <form id="resetEmail" action='/' method='post' >
             <input  name="_csrf" type="hidden" th:value="${_csrf.token}">
             <input  name="_csrf_header" type="hidden" th:value="${_csrf.headerName}"/>
             <input type="email" class="form-control  col-md-12" id="emailOut" name="userEmail" style="width:360px" placeholder="请输入新邮箱">
              <input type="password" class="form-control  col-md-12" id="pwd1" name="userpwd1" style="width:360px" placeholder="请输入密码确认">
              <button id="putEmail" type='button'>提交</button>
          </form>
       </div>
    </div>
~~~

  1、通过ajax 提交数据，更新数据成功后弹窗。

  2、后台从session中获取用户，只能修改本人信息

  3、非本人无法进入修改页面

 ### 修改其他信息同理





## 网站管理

1. 管理员可以删掉或者回复用户问题

2. 管理员可以对用户封禁和解除封禁

3. 管理员可以通过用户名查找或者邮箱查找进入某个用户管理界面

   ~~~java
    @RequestMapping("search/{userPn}/{page}")
       public String manage2(HttpServletRequest request, Model model,@PathVariable int page,@PathVariable String userPn){
           HttpSession session=request.getSession();
           //获取登录用户
           User userLogin;
           String userName = (String)session.getAttribute("userName");
           if(userName!=null){
               userLogin = userService.getUserByUserName(userName);
           }else{
               return "404";
           }
           //查看是否为管理员
           if(userLogin.getUserRole()!=1){
               return "404";
           }
           //获取被查看用户信息
           User user;
           if(userPn.contains("@")){
               user=userService.selectByEmail(userPn);
           }else{
               user = userService.getUserByUserName(userPn);
           }
   
           //得到问题
           //获取提问箱问题数
           List<Question> askedlist = questionService.selectAsked(user.getUserId());
           int askedNumber = askedlist.size();
           //获取提问数
           List<Question> askList = questionService.selectAsk(user.getUserId());
           int askNumber = askList.size();
           //获取回复的数量
           List<Question> respenseList = questionService.selectNotNull(user.getUserId());
           int respendNumber = respenseList.size();
           List<Question> list1 = askList;
           List<Question> list2 = askedlist;
           List<Question> list3 = respenseList;
           int pageAll = askedlist.size()/4+1;
           int askedlistPageAll = askedlist.size();
           int askListPageAll = askList.size();
           int respenseListPageAll = respenseList.size();
   
   
           //分页
          if(askedlist.size()>page*4){
               askedlist = askedlist.subList(page*4-4,page*4);
           }else{
               askedlist=askedlist.subList(page*4-4,list2.size());
           }
   
           if(askList.size()>page*4){
               askList = askList.subList(page*4-4,page*4);
           }else{
               askList=askList.subList(page*4-4,list1.size());
           }
   
   
          if(respenseList.size()>page*4){
               respenseList = respenseList.subList(page*4-4,page*4);
           }else{
               respenseList=respenseList.subList(page*4-4,list3.size());
           }
   
   
           model.addAttribute("user", user);
           model.addAttribute("active", user.getUserActive()==ACTIVATION?"已激活":"未激活");
           model.addAttribute("ban", user.getUserBan()==USER_BAN?"封禁中":"未被封禁");
           model.addAttribute("userLogin",userLogin);
           model.addAttribute("page",page);
           model.addAttribute("askedList",askedlist);
           model.addAttribute("pageAll",pageAll);
           model.addAttribute("askedlistPageAll",askedlistPageAll);
           model.addAttribute("askList",askList);
           model.addAttribute("askListPageAll",askListPageAll);
           model.addAttribute("respenseList",respenseList);
           model.addAttribute("respenseListPageAll",respenseListPageAll);
           model.addAttribute("flag",userLogin.getUserId()==user.getUserId()?"true":"false");
           model.addAttribute("path","manage");
           model.addAttribute("flag1",userLogin.getUserId()==user.getUserId()&&userLogin.getUserRole()==1?"true":"false");
           model.addAttribute("pathOne","/quiryBox/"+user.getUserName()+"/1");
   
           return "search";
       }
   ~~~

## 安全

### XSS与CSRF



1.防御XSS

~~~java
@WebFilter(filterName="myXssFilter", urlPatterns="/*")
public class BoxFilter implements Filter {

    FilterConfig filterConfig = null;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        filterChain.doFilter(new XssHttpServletRequestWrapper((HttpServletRequest)servletRequest), servletResponse);
    }

    @Override
    public void destroy() {
        this.filterConfig = null;
    }
}

~~~



2.防御CSRF

~~~java
public UserLoginFilter(SessionIdService sessionIdService, UserService userService) {
        this.sessionIdService = sessionIdService;
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session=request.getSession();
        String userName = (String)session.getAttribute("userName");
        System.out.println(userName);
        User user = userService.getUserByUserName(userName);
        //sessionID和数据库里面的是否相等
        if(session.getId().equals(sessionIdService.selecetSessionByUserId( user.getUserId() ).getSessionID() ) ){
            return true;
        }
        return false;
    }

~~~

用户登录时将sessionID存入数据，如果已经有了则覆盖，每次请求检验这个sessionID。



## 密码安全

~~~java
public class CreateRand {
    public static String getGUID() {
        StringBuilder uid = new StringBuilder();
        //产生16位的强随机数
        Random rd = new SecureRandom();
        for (int i = 0; i < 16; i++) {
            //产生0-2的3位随机数
            int type = rd.nextInt(3);
            switch (type){
                case 0:
                    //0-9的随机数
                    uid.append(rd.nextInt(10));
                    break;
                case 1:
                    //ASCII在65-90之间为大写,获取大写随机
                    uid.append((char)(rd.nextInt(25)+65));
                    break;
                case 2:
                    //ASCII在97-122之间为小写，获取小写随机
                    uid.append((char)(rd.nextInt(25)+97));
                    break;
                default:
                    break;
            }
        }
        return uid.toString();
    }
~~~



生成随机乱码，存在数据库，修改密码需要检验该码是否正确。











