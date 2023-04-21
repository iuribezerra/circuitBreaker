package old.spring.cb.controller;

import old.spring.cb.retrofit.RetrofitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping("api")
public class ApiController {

    @Autowired
    private RetrofitService retrofitService;

    @GetMapping("/status/{code}")
    public ResponseEntity<?> status(@PathVariable("code") int code) throws IOException {
        return retrofitService.callStatus(code);
    }

    @GetMapping("/delay/{delay}")
    public ResponseEntity<?> delay(@PathVariable("delay") int delay) throws IOException {
        return retrofitService.callDelay(delay);
    }
}