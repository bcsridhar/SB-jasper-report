package com.sriharsha.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import com.sriharsha.model.Employee;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

	@GetMapping("/report")
	public ResponseEntity<byte[]> getReport() {
		
		try {
			//String filePath = "D:\\projects\\springboot\\Jasper-Report\\src\\main\\resources\\FirstReport.jrxml";
			String filePath = ResourceUtils.getFile("classpath:FirstReport.jrxml").getAbsolutePath();
			
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("companyName", "Lifafa Gifttech Pvt Ltd.");
			parameters.put("companyAddress", "91Springboard, 175 & 176, Dollars Colony, Phase 4, J. P. Nagar, Bengaluru, Karnataka 560078");
			
			Employee employee1 = new Employee(1L, "Raj", "Joshi", "Happy Street", "Delhi");
			
			Employee employee2 = new Employee(2L, "Peter", "Smith", "Any Street", "Mumbai");
			
			List<Employee> list = new ArrayList<Employee>();
			list.add(employee1);
			list.add(employee2);
			
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);
			
			JasperReport report = JasperCompileManager.compileReport(filePath);
			
			JasperPrint print = JasperFillManager.fillReport(report, parameters, dataSource);
			
			// JasperExportManager.exportReportToPdfFile(print, "D:\\projects\\springboot\\data\\exported-reports\\firstreport.pdf");
			// As pdf
			byte[] byteArray = JasperExportManager.exportReportToPdf(print);
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_PDF);
			headers.setContentDispositionFormData("filename", "employee.pdf");
			
			return new ResponseEntity<byte[]>(byteArray, headers, HttpStatus.OK);
			
		} catch (Exception e) {
			System.out.println("Something went wrong " + e);
			return new ResponseEntity<byte[]>(HttpStatus.INTERNAL_SERVER_ERROR);
		}	
	}
}
