package com.example.voting;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/votes")
public class VoteController {
    private final Map <String , Integer> partijStemmen = new HashMap <>();

    @GetMapping
    public Map<String, Integer> getPartijStemmen (){

        return partijStemmen;
    }

    @PostMapping
    public ResponseEntity<String> postPartij (@RequestBody String partij){
      partijStemmen.merge(partij, 1,Integer::sum);

        return ResponseEntity.ok("" + partij);
    }
}
