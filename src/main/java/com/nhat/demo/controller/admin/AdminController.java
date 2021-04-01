package com.nhat.demo.controller.admin;

import com.lowagie.text.DocumentException;
import com.nhat.demo.config.ExportBill;
import com.nhat.demo.config.ReportBookingExcelExporter;
import com.nhat.demo.entity.*;
import com.nhat.demo.repository.*;
import com.nhat.demo.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Controller
@Slf4j
@Transactional
@RequestMapping(value = "/admin")
public class AdminController {
    public static String uploadDirectory = System.getProperty("user.dir") + "/src/main/resources/static/images/room";
    @Autowired
    RoomTypeRepository roomTypeRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    RoomtypeImageRepository roomtypeImageRepository;

    @Autowired
    RoomServiceIF roomServiceIF;

    @Autowired
    HotelSVServiceIF hotelSVService;

    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    BookingServiceIF bookingService;

    @Autowired
    ChargeServiceIF chargeService;

    @Autowired
    RoomTypeServiceIF roomTypeServiceIF;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    EncryptPassword encryptPassword;

    @Autowired
    ChargeRepository chargeRepository;


    @Autowired
    CreditCardServiceIF creditCardService;

    @Autowired
    BookingDetailRepository bookingDetailRepository;

    @RequestMapping(method = RequestMethod.GET)
    public String showAdminPage() {
        return "/manage/adminPage";
    }

    @GetMapping("addNewRoom")
    public String addNewRoom(Model model) {
        List<RoomType> roomTypeList = roomTypeRepository.findAll();
        Room room = new Room();
        model.addAttribute("room", room);
        model.addAttribute("roomTypeList", roomTypeList);
        return "/manage/addNewRoom";
    }

    @PostMapping("saveRoom")
    public String saveRoom(@ModelAttribute Room room, Model model) {
        Room newRoom = roomRepository.findByRoomNumber(room.getRoomNumber());
        if (newRoom != null) {
            model.addAttribute("errorMessage", "Phòng đã tồn tại!!!");
            List<RoomType> roomTypeList = roomTypeRepository.findAll();
            model.addAttribute("room", room);
            model.addAttribute("roomTypeList", roomTypeList);
            return "/manage/addNewRoom";
        }
        roomRepository.save(room);
        return "redirect:/admin/viewAllRoom";
    }

    @GetMapping("editRoom/{roomId}")
    public String editRoom(Model model, @PathVariable("roomId") int roomId) {
        List<BookingDetail> bookingDetails = bookingDetailRepository.getListBookingDetailByRoomId(roomId);
        if(bookingDetails.size() == 0){
            Room room = roomRepository.findById(roomId).orElse(null);
            List<RoomType> roomTypeList = roomTypeRepository.findAll();

            model.addAttribute("room", room);
            model.addAttribute("roomTypeList", roomTypeList);
            return "/manage/editRoom";
        }

        model.addAttribute("errorMessage", "Phòng đã được đặt không được chỉnh sửa !!!");
        return viewAllRoom(model);


    }

    @PostMapping("saveEditRoom")
    public String saveEditRoom(@ModelAttribute Room room) {
//        Room newRoom = roomRepository.findByRoomNumber(room.getRoomNumber());
//        if(newRoom != null){
//            model.addAttribute("errorMessage","Phòng đã tồn tại!!!");
//            List<RoomType> roomTypeList = roomTypeRepository.findAll();
//            model.addAttribute("room",room);
//            model.addAttribute("roomTypeList",roomTypeList);
//            return "/manage/editRoom";
//        }
        roomRepository.save(room);
        return "redirect:/admin/viewAllRoom";
    }

    @GetMapping("deleteRoom/{roomId}")
    public String deleteRoom(@PathVariable("roomId") int roomId,Model model) {
        List<BookingDetail> bookingDetails = bookingDetailRepository.getListBookingDetailByRoomId(roomId);
        if(bookingDetails.size() == 0){
            roomRepository.deleteById(roomId);
            return "redirect:/admin/viewAllRoom";
        }
        model.addAttribute("errorMessage", "Phòng đã được đặt không được xóa !!!");
        return viewAllRoom(model);

    }


    @GetMapping("addNewRoomType")
    public String addNewRoomType(Model model) {
        RoomType roomType = new RoomType();
        model.addAttribute("roomType", roomType);
        return "/manage/addNewRoomType";
    }

    @PostMapping("saveNewRoomType")
    public String saveRoomType(@ModelAttribute RoomType roomType, @RequestParam MultipartFile[] files, Model model) {
        RoomType newRoomType = roomTypeRepository.findByTypeName(roomType.getTypeName());
        if (newRoomType != null) {
            model.addAttribute("errorMessage", "Tên loại phòng đã tồn tại!!!");
            model.addAttribute("roomType", roomType);
            return "/manage/addNewRoomType";
        }
        roomTypeRepository.save(roomType);
        StringBuffer fileNames = new StringBuffer();
        for (MultipartFile file : files) {
            Path fileNameAndPath = Paths.get(uploadDirectory, file.getOriginalFilename());
            fileNames.append(file.getOriginalFilename());
            try {
                Files.write(fileNameAndPath, file.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            RoomTypeImage roomTypeImage = new RoomTypeImage();
            roomTypeImage.setRoomType(roomType);
            roomTypeImage.setPath("/images/room/" + file.getOriginalFilename());
            roomtypeImageRepository.save(roomTypeImage);
        }
        return "redirect:/admin/viewAllRoomType";
    }

    @GetMapping("/viewAllRoom")
    public String viewAllRoom(Model model) {
        return listByPage(model, 1);
    }

    @GetMapping("viewAllRoom/page/{pageNumber}")
    public String listByPage(Model model, @PathVariable("pageNumber") int currentPage) {
        Page<Room> rooms = roomServiceIF.getAllRoom(currentPage);
        int totalPages = rooms.getTotalPages();
        long totalItems = rooms.getTotalElements();
        model.addAttribute("rooms", rooms);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("currentPage", currentPage);

        return "/manage/viewAllRoom";
    }

    @GetMapping("/viewAllRoomType")
    public String viewAllRoomType(Model model) {

        return listByRoomTypePage(model, 1);
    }

    @GetMapping("viewAllRoomType/page/{pageNumber}")
    public String listByRoomTypePage(Model model, @PathVariable("pageNumber") int currentPage) {
        Page<RoomType> roomTypes = roomTypeServiceIF.getAllRoomTypePage(currentPage);
        int totalPages = roomTypes.getTotalPages();
        long totalItems = roomTypes.getTotalElements();
        model.addAttribute("roomTypes", roomTypes);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("currentPage", currentPage);

        return "/manage/viewAllRoomType";
    }

    @PostMapping("searchRoom")
    public String searchRoom(Model model, @RequestParam("searchRoom") String searchText) {
        List<Room> rooms = roomServiceIF.getRoomSearch(searchText);
        if (rooms == null) {
            model.addAttribute("errorMessage", "Không tìm thấy kết quả tương ứng!!!");
            return "/manage/viewSearchRoom";
        }
        model.addAttribute("rooms", rooms);
        return "/manage/viewSearchRoom";
    }

    @PostMapping("searchRoomType")
    public String searchRoomType(Model model, @RequestParam("searchRoomType") String searchText) {
        List<RoomType> roomTypes = roomTypeServiceIF.getSearchRoomType(searchText);
        if (roomTypes == null) {
            model.addAttribute("errorMessage", "Không tìm thấy kết quả tương ứng!!!");
            return "/manage/viewSearchRoomType";
        }
        model.addAttribute("roomTypes", roomTypes);
        return "/manage/viewSearchRoomType";
    }

    @GetMapping("editRoomType/{roomTypeId}")

    public String editRoomType(Model model, @PathVariable("roomTypeId") int roomTypeId) {
        RoomType roomType = roomTypeRepository.findById(roomTypeId).orElse(null);
        model.addAttribute("roomType", roomType);

        return "/manage/editRoomType";
    }

    @PostMapping("saveEditRoomType")
    public String saveEditRoomType(@ModelAttribute RoomType roomType, @RequestParam MultipartFile[] files, Model model) {
//        RoomType newRoomType = roomTypeRepository.findByTypeName(roomType.getTypeName());
//        if(newRoomType != null){
//            model.addAttribute("errorMessage","Tên loại phòng đã tồn tại!!!");
//            model.addAttribute("roomType",roomType);
//            return "/manage/editRoomType";
//        }
        roomTypeRepository.save(roomType);
        roomtypeImageRepository.deleteRoomTypeImageFromRoomTypeId(roomType.getRoomTypeId());

        StringBuffer fileNames = new StringBuffer();
        for (MultipartFile file : files) {
            Path fileNameAndPath = Paths.get(uploadDirectory, file.getOriginalFilename());
            fileNames.append(file.getOriginalFilename());
            try {
                Files.write(fileNameAndPath, file.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            RoomTypeImage roomTypeImage = new RoomTypeImage();
            roomTypeImage.setRoomType(roomType);
            roomTypeImage.setPath("/images/room/" + file.getOriginalFilename());
            roomtypeImageRepository.save(roomTypeImage);
        }
        return "redirect:/admin/viewAllRoomType";
    }

    @GetMapping("deleteRoomType/{roomTypeId}")
    public String deleteRoomType(@PathVariable("roomTypeId") int roomTypeId) {
        roomTypeRepository.deleteById(roomTypeId);
        return "redirect:/admin/viewAllRoomType";
    }

    @GetMapping("viewAllAccount")
    public String viewAllAccount(Model model) {
        List<Account> accounts = accountRepository.findAll();
        model.addAttribute("accounts", accounts);
        return "/manage/viewAllAccount";
    }

    @GetMapping("deleteAccount/{id}")
    public String deleteAccount(@PathVariable int id,Model model, Principal principal) {
//        request.userPrincipal.getName()
        String currentName = principal.getName();
        Account account = accountRepository.findById(id).orElse(null);
        if(account.getUsername().equals(currentName)){
            String errorMessage = null;
            model.addAttribute("errorMessage","Tài khoản đang đăng nhập không được xóa");
            List<Account> accounts = accountRepository.findAll();
            model.addAttribute("accounts", accounts);
            return "/manage/viewAllAccount";
        }
        accountRepository.deleteById(id);
        return "redirect:/admin/viewAllAccount";
    }

    @GetMapping("addNewAccount")
    public String addNewAccount(Model model) {
        Account account = new Account();
        List<Role> roles = roleRepository.findAll();
        model.addAttribute("account", account);
        model.addAttribute("roles", roles);
        return "/manage/addNewAccount";
    }

    @PostMapping("saveAccount")
    public String saveAccount(@ModelAttribute("account") Account account, Model model) {
        Account newAccount = accountRepository.findByUsername(account.getUsername());
        if (newAccount != null) {
            model.addAttribute("errorMessage", "Tài khoản đã tồn tại!!!");
            List<Role> roles = roleRepository.findAll();
            model.addAttribute("account", account);
            model.addAttribute("roles", roles);
            return "/manage/addNewAccount";
        }
        String encodePassword = EncryptPassword.encrypt(account.getPassword());
        account.setPassword(encodePassword);
        accountRepository.save(account);
        return "redirect:/admin/viewAllAccount";
    }

    @GetMapping("viewReportBooking")
    public String viewReportBookingPage(Model model) {
        Booking booking = new Booking();
        model.addAttribute("booking", booking);
        return "manage/viewReportBooking";
    }


    @GetMapping({"searchBookingDate", "searchBookingDate/{type}"})
    public String searchBookingDate(
            @RequestParam(value = "fromDate", required = false) String fromDate,
            @RequestParam(value = "toDate", required = false) String toDate, Model model, HttpServletRequest request,
            HttpServletResponse response,
            @PathVariable(value = "type", required = false) String type) throws IOException {


        List<Booking> bookings = null;

        if (type == null) {
            LocalDate newFromDate = LocalDate.parse(fromDate);
            LocalDate newToDate = LocalDate.parse(toDate);
            bookings = bookingRepository.getListSearchBookingFromTo(newFromDate, newToDate);
            request.getSession().setAttribute("bookings", bookings);

        } else if (type.equals("excel")) {
            bookings = (List<Booking>) request.getSession().getAttribute("bookings");
            response.setContentType("application/octet-stream");
            DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
            String currentDateTime = dateFormatter.format(new Date());

            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=booking_" + currentDateTime + ".xlsx";
            response.setHeader(headerKey, headerValue);

            ReportBookingExcelExporter excelExporter = new ReportBookingExcelExporter(bookings);

            excelExporter.export(response);
        }

        double sumTotal = bookings.stream().mapToDouble(Booking::getTotal).sum();
        model.addAttribute("bookings", bookings);
        model.addAttribute("sumTotal", sumTotal);
        return "/manage/viewReportBooking";

    }


    @GetMapping("/viewAllService")
    public String viewAllService() {
        return "forward:/admin/viewAllService/page/1";
    }

    @GetMapping("/viewAllService/page/{pageNumber}")
    public String listServiceByPage(Model model, @PathVariable("pageNumber") int currentPage) {
        Page<Service> services = hotelSVService.getAllserviceByPage(currentPage);
        int totalPages = services.getTotalPages();
        long totalItems = services.getTotalElements();
        model.addAttribute("services", services);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("currentPage", currentPage);

        return "/manage/viewAllService";
    }

    @GetMapping("/addNewService")
    public String addNewService(Model model) {
        model.addAttribute("service", new Service());
        return "/manage/serviceForm";
    }

    @PostMapping("/saveOrUpdate")
    public String saveOrUpdate(@Valid @ModelAttribute Service service, BindingResult result) {
        if (result.hasErrors()) {
            return "/manage/serviceForm";
        }
        serviceRepository.save(service);
        return "redirect:/admin/viewAllService";
    }


    @GetMapping("/deleteService/{serviceId}")
    public String deleteService(@PathVariable int serviceId) {
        serviceRepository.deleteById(serviceId);
        return "redirect:/admin/viewAllService";
    }

    @GetMapping("/editService/{serviceId}")
    public String editService(@PathVariable int serviceId, Model model) {
        model.addAttribute("service", serviceRepository.findById(serviceId));
        return "/manage/serviceForm";
    }


    @GetMapping("/viewCurrentStayBooking")
    public String viewCurrentStayBooking(Model model) {
        // lay danh sach cac booking hien dang o khach san tai ngay hien tai
        List<Booking> currentStayBookings = bookingService.getCurrentStayBooking();
        //lay danh muc dich vu
        List<Service> services = serviceRepository.findAll();

        model.addAttribute("bookings", currentStayBookings);
        model.addAttribute("services", services);
        return "/manage/viewCurrentStayBooking";
    }


    @PostMapping("/addServiceToBooking")
    public String addServiceToBooking(Charge charge,
                                      Model model) {
        chargeService.addCharge(charge);
        return "redirect:/admin/viewCurrentStayBooking";
    }

//        @GetMapping({"/viewDetailConsumedService/{bookingId}","viewDetailConsumedService/pdf"})
//    public  String viewDetailConsumedService(@PathVariable(value = "bookingId", required = false) int bookingId,
//                                            @RequestParam(value = "pdf",required = false) String type,
//                                            Model model,HttpServletRequest request,
//                                            HttpServletResponse response) throws IOException, DocumentException {
//        Booking booking = null;
//        List<Charge> charges = null;
//        if(type == null) {
//            booking = bookingService.getBookingById(bookingId);
//            request.getSession().setAttribute("booking", booking);
//            charges = chargeRepository.getListChargeByBookingId(booking.getBookingId());
//            request.getSession().setAttribute("charges", charges);
//        } else if(type.equals("pdf"))
//        {
//            response.setContentType("application/pdf");
//            DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
//            String currentDateTime = dateFormatter.format(new Date());
//
//            String headerKey = "Content-Disposition";
//            String headerValue = "attachment; filename=users_" + currentDateTime + ".pdf";
//            response.setHeader(headerKey, headerValue);
//
//            request.getSession().getAttribute("booking");
//            request.getSession().getAttribute("charges");
//
//            ExportBill exportBill = new ExportBill(booking,charges);
//
//            exportBill.export(response);
//        }
//        model.addAttribute("booking", booking);
//        model.addAttribute("charges", charges);
//        return "/manage/viewDetailConsumedService";
//    }

    @GetMapping("/viewDetailConsumedService/{bookingId}")
    public String viewDetailConsumedService(@PathVariable(value = "bookingId", required = false) int bookingId,
                                            Model model) {
        Booking booking = null;
        List<Charge> charges = null;

        booking = bookingService.getBookingById(bookingId);

        charges = chargeRepository.getListChargeByBookingId(booking.getBookingId());


        model.addAttribute("booking", booking);
        model.addAttribute("charges", charges);
        return "/manage/viewDetailConsumedService";
    }


    @GetMapping("viewDetailConsumedService/pdf")
    public String exportPdf(@RequestParam int bookingId, Model model, HttpServletRequest request,
                            HttpServletResponse response) throws IOException, DocumentException {
        Booking booking = null;
        List<Charge> charges = null;

        booking = bookingService.getBookingById(bookingId);

        charges = chargeRepository.getListChargeByBookingId(booking.getBookingId());

        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=bill_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        request.getSession().getAttribute("booking");
        request.getSession().getAttribute("charges");

        ExportBill exportBill = new ExportBill(booking, charges);

        exportBill.export(response);

        model.addAttribute("booking", booking);
        model.addAttribute("charges", charges);
        return "/manage/viewDetailConsumedService";
    }

    @GetMapping("/viewPayPage/{bookingId}")
    public String viewPayPage(Model model,@PathVariable int bookingId) {
        model.addAttribute("bookingId",bookingId);
        return "/manage/payPage";

    }

    @PostMapping("/checkOut")
    public String checkOut(@RequestParam String ownerName,
                           @RequestParam String cardNumber,
                           @RequestParam  String expiryMonth,
                           @RequestParam String expiryYear,
                           @RequestParam int bookingId,
                           Model model) {

        double totalCharge = bookingService.getBookingById(bookingId).getTotalCharge();


        CreditCard creditCard = new CreditCard();

        creditCard.setOwnerName(ownerName);
        creditCard.setCardNumber(cardNumber);
        creditCard.setExpiryMonth(expiryMonth);
        creditCard.setExpiryYear(expiryYear);

        // kiem tra tinh trang cua CreditCart
        String result = creditCardService.ValidateCart(creditCard);
        if (result.equals("not match") || result.equals("not enough")) {
            model.addAttribute("message", "Thông tin thẻ không chính xác, hoặc không đủ tiền");
            return viewPayPage(model, bookingId);
//            return "redirect:/admin/viewPayPage/" + bookingId;
        }


        creditCardService.tranferMoney(creditCard.getCardNumber(), CreditCardServiceIF.HOTELCARD, totalCharge);
        return "/manage/checkOutSuccess";

    }


}


