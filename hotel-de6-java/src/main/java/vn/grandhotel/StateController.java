package vn.grandhotel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController @RequestMapping("/api/state") public class StateController {
 private final JdbcTemplate db;private final ObjectMapper json;
 public StateController(JdbcTemplate db,ObjectMapper json){this.db=db;this.json=json;}
 @GetMapping public synchronized Map<String,String> get(){Map<String,String> result=new LinkedHashMap<>();db.query("SELECT state_key,state_value FROM hotel_state",rs->{result.put(rs.getString(1),rs.getString(2));});return result;}
 @PutMapping public synchronized Map<String,Object> put(@RequestBody Map<String,String> updates){return save(updates);}
 @PostMapping("/beacon") public synchronized Map<String,Object> beacon(@RequestBody Map<String,String> updates){return save(updates);}
 private Map<String,Object> save(Map<String,String> updates){int count=0;for(var row:updates.entrySet()){String k=row.getKey(),value=row.getValue();if(!k.startsWith("grandhotel_")||k.length()>120||value==null||value.length()>2000000)continue;db.update("INSERT INTO hotel_state(state_key,state_value,updated_at) VALUES(?,?,NOW()) ON CONFLICT(state_key) DO UPDATE SET state_value=EXCLUDED.state_value,updated_at=NOW()",k,value);count++;}return Map.of("saved",count);}
}
