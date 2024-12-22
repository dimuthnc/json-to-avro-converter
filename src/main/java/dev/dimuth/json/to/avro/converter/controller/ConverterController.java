package dev.dimuth.json.to.avro.converter.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.dimuth.json.to.avro.converter.service.JSONToAVROConversionService;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

@RestController("/convert")
public class ConverterController {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/json-to-avro")
    public ResponseEntity<byte[]> convertJsonToAvro(@RequestBody String requestBody) throws JsonProcessingException {
        JSONObject jsonObject = new JSONObject(requestBody);

        try {
            byte[] avroData = JSONToAVROConversionService.convertJsonToAvro(jsonObject.get("json").toString(), jsonObject.get("schema").toString());
            System.out.println(avroData.toString());
            return ResponseEntity.ok().body(avroData);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/avro-to-json")
    public ResponseEntity<String> convertAvroToJson(@RequestBody String requestBody) {
        JSONObject jsonObject = new JSONObject(requestBody);

        try {
            String avroSchemaString = jsonObject.getJSONObject("schema").toString();
            byte[] avroData = convertStringToAvroData(jsonObject.getString("avroData"));

            // Parse the Avro schema
            Schema schema = new Schema.Parser().parse(avroSchemaString);

            // Deserialize Avro data to GenericRecord
            DatumReader<GenericRecord> datumReader = new GenericDatumReader<>(schema);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(avroData);
            Decoder decoder = DecoderFactory.get().binaryDecoder(inputStream, null);
            GenericRecord record = datumReader.read(null, decoder);

            return ResponseEntity.ok().body(record.toString());
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] convertStringToAvroData(String avroDataString) {
        // Remove the square brackets
        String cleanedString = avroDataString.replaceAll("[\\[\\] ]", "");
        // Split the string by commas
        String[] byteValues = cleanedString.split(",");
        // Convert each element to a byte and store it in a byte array
        byte[] avroData = new byte[byteValues.length];
        for (int i = 0; i < byteValues.length; i++) {
            avroData[i] = Byte.parseByte(byteValues[i]);
        }
        return avroData;
    }
}
