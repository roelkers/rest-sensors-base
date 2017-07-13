
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;

import de.tub.ise.anwsys.Server;
import de.tub.ise.anwsys.models.Measurement;
import de.tub.ise.anwsys.models.Metric;
import de.tub.ise.anwsys.models.SmartMeter;
import de.tub.ise.anwsys.repos.MeasurementRepository;
import de.tub.ise.anwsys.repos.MeterRepository;
import de.tub.ise.anwsys.repos.MetricRepository;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Server.class)
@WebAppConfiguration
public class MeterControllerTest {

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private String userName = "bdussault";

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private List<SmartMeter>meterList = new ArrayList<>(); 
    
    @Autowired
    private MeterRepository meterRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MeasurementRepository measurementRepository;

    //@Autowired
    //private MetricRepository metricRepository;
    
    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
            .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
            .findAny()
            .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }
    
    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();

       
        /*
        this.meterList.add(meterRepository.save(new SmartMeter("ise1224hi5630")));
        this.meterList.add(meterRepository.save(new SmartMeter("ise1224hi5631")));
        this.meterList.add(meterRepository.save(new SmartMeter("ise1224hi5632")));
        
        meterList.forEach(meter ->{
        	Metric metric1 = new Metric("MX-11460-01","Current(mA)",meter);
			Metric metric2 = new Metric("MX-11564-01","Voltage(V)",meter);
			meter.getMetrics().put(metric1.getId(), metric1);
			meter.getMetrics().put(metric2.getId(), metric2);
        });*/
        
        this.meterList = (List<SmartMeter>) meterRepository.findAll();
        
        //System.out.println("METRICS-SIZE"+this.meterList.get(0).getMetrics().values().size());
        //System.out.println("METRICS-SIZE"+this.meterList.get(1).getMetrics().values().size());
        //System.out.println("METRICS-SIZE"+this.meterList.get(2).getMetrics().values().size());
        
        //System.out.println("METRICS-SIZE"+this.meterList.get(0).getMetrics().get
    }
    
    @Test
    public void getMeters() throws Exception {
    	MvcResult result =
    			mockMvc.perform(get("/meters"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$[0].id", is(this.meterList.get(0).getId())))
                .andExpect(jsonPath("$[1].id", is(this.meterList.get(1).getId())))
                //.andExpect(jsonPath("$[0].metrics['MX-11460-01'].id", is("MX-11460-01")))
                //.andExpect(jsonPath("$[0].metrics['MX-11564-01'].id", is("MX-11564-01")))
                .andReturn();
    	System.out.println("response: "+result.getResponse().getContentAsString());
    }
    
    @Test
    public void getSingleMeter() throws Exception {
    	MvcResult result = mockMvc.perform(get("/meters/" + this.meterList.get(0).getId()))
    	.andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andExpect(jsonPath("$.id", is(this.meterList.get(0).getId())))
        .andReturn();
    	
    	System.out.println("responseSingle: "+result.getResponse().getContentAsString());
    }
    
    @Test
    public void getAllMetrics() throws Exception {
    	MvcResult result = mockMvc.perform(get("/meters/" + this.meterList.get(0).getId()+"/metrics"))
    	.andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        
        .andReturn();
    	
    	//System.out.println("responseMetrics: "+result.getResponse().getContentAsString());
    }
	
    @Test
    public void postMeasurement() throws Exception {
    	long timeMillis = System.currentTimeMillis();
    	double value = 1000;
    	//String metricId = "1";
    	//Metric metric = this.meterList.get(0).getMetrics().get("1");
    	//Measurement mes = new Measurement(timeMillis, value);
    	
    	//System.out.println("metrics: " + this.meterList.get(0).getMetrics().get);
    	//System.out.println(metric.toString());
    	
        //String measurementJson = json(mes);

        this.mockMvc.perform(post("/meters/" + meterList.get(0).getId() + "/metrics/" + meterList.get(0).getMetrics().get("MX-11460-01") +"/measurements")
        		.param("timeMillis", Long.toString(timeMillis))
        		.param("value", Double.toString(value))
        		)
                .andExpect(status().isCreated());
    }	
    
    
    @Test
    public void getAverageMetric() throws Exception {
    	/*Metric metric = this.meterList.get(0).getMetrics().get(0);
    	Measurement mes1 = new Measurement(metric, System.currentTimeMillis(), 100);
    	Measurement mes2 = new Measurement(metric, System.currentTimeMillis()+1000, 300);
    	
    	measurementRepository.save(mes1);
    	measurementRepository.save(mes2);*/
    	
    	long timeMillis = System.currentTimeMillis();
    	
    	this.mockMvc.perform(post("/meters/" + meterList.get(0).getId() + "/metrics/MX-11460-01/measurements")
        		.param("timeMillis", Long.toString(timeMillis))
        		.param("value", "100")
        		)
                .andExpect(status().isCreated());
    	
    	this.mockMvc.perform(post("/meters/" + meterList.get(0).getId() + "/metrics/MX-11460-01/measurements")
        		.param("timeMillis", Long.toString(timeMillis))
        		.param("value", "300")
        		)
                .andExpect(status().isCreated());
    	
    	MvcResult result =mockMvc.perform(get("/meters/" + meterList.get(0).getId() + "/metrics/MX-11460-01")
    			.param("timeMillisMeasurement", Long.toString(System.currentTimeMillis()))
    			)
    			.andExpect(status().isOk())
    			.andExpect(content().contentType(contentType))
    			.andExpect(jsonPath("$.metricName", is("MX-11460-01")))
    	    	.andExpect(jsonPath("$.value", is(200.0)))
    	    	.andExpect(jsonPath("$.sampleSize", is(2)))
    	    	.andReturn();
    	
    	System.out.println("responseAverage: "+result.getResponse().getContentAsString());
    	
    }
    
    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
    
}
