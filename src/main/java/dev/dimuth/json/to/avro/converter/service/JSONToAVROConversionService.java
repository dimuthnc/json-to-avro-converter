package dev.dimuth.json.to.avro.converter.service;



import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.io.DatumWriter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;


public class JSONToAVROConversionService {
    public static byte[] convertJsonToAvro(String jsonString, String avroSchemaString) throws Exception {
        // Parse the Avro schema
        Schema schema = new Schema.Parser().parse(avroSchemaString);

        // Parse the JSON object
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonString);

        // Create GenericRecord from JSON
        GenericRecord record = createGenericRecord(jsonNode, schema);

        // Log the Avro JSON message
        logAvroAsJson(record, schema);

        // Serialize to Avro byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<>(schema);
        Encoder encoder = EncoderFactory.get().binaryEncoder(outputStream, null);
        datumWriter.write(record, encoder);
        encoder.flush();
        outputStream.close();

        return outputStream.toByteArray();
    }

    private static void logAvroAsJson(GenericRecord record, Schema schema) throws Exception {
        ByteArrayOutputStream jsonOutputStream = new ByteArrayOutputStream();
        DatumWriter<GenericRecord> jsonDatumWriter = new GenericDatumWriter<>(schema);
        Encoder jsonEncoder = EncoderFactory.get().jsonEncoder(schema, jsonOutputStream);
        jsonDatumWriter.write(record, jsonEncoder);
        jsonEncoder.flush();
        jsonOutputStream.close();

        String avroJsonMessage = jsonOutputStream.toString();
        System.out.println("Avro JSON Message: " +  avroJsonMessage); // Log the Avro JSON message
    }

    private static GenericRecord createGenericRecord(JsonNode jsonNode, Schema schema) {
        GenericRecord record = new GenericData.Record(schema);

        for (Schema.Field field : schema.getFields()) {
            String fieldName = field.name();
            Schema fieldSchema = field.schema();
            JsonNode fieldValue = jsonNode.get(fieldName);

            // Handle nested objects and arrays
            Object value = getValue(fieldValue, fieldSchema);
            record.put(fieldName, value);
        }

        return record;
    }

    private static Object getValue(JsonNode jsonNode, Schema schema) {
        if (jsonNode == null) {
            return null;
        }

        switch (schema.getType()) {
            case RECORD:
                return createGenericRecord(jsonNode, schema);
            case ARRAY:
                Schema elementType = schema.getElementType();
                GenericData.Array<Object> array = new GenericData.Array<>(jsonNode.size(), schema);
                for (JsonNode element : jsonNode) {
                    array.add(getValue(element, elementType));
                }
                return array;
            case STRING:
                return jsonNode.asText();
            case INT:
                return jsonNode.asInt();
            case LONG:
                return jsonNode.asLong();
            case FLOAT:
                return jsonNode.floatValue();
            case DOUBLE:
                return jsonNode.doubleValue();
            case BOOLEAN:
                return jsonNode.asBoolean();
            default:
                return null;
        }
    }
}
