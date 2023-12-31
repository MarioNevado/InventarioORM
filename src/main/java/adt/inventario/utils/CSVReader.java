package adt.inventario.utils;

import adt.inventario.model.Product;
import adt.inventario.pojo.ProductPojo;

import java.io.*;

public class CSVReader {
    File file;

    public CSVReader(File file) throws IOException, NumberFormatException {
        if (!file.exists() || !file.isFile()) {
            throw new FileNotFoundException("El archivo no existe o no es un archivo");
        } else {
            this.file = file;
            readCSV();
        }
    }

    public void readCSV() throws IOException, NumberFormatException {//si ya esta añadido ese producto aumenta la cantidad
        String line = "";
        Product product;
        ProductPojo pojo = new ProductPojo();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine();
            while((line = reader.readLine()) != null){
                line = line.toLowerCase().trim();
                product = new Product(line.split(";", 2)[1], Integer.parseInt(line.split(";", 2)[0]));
                if (pojo.isNew(product)){
                    pojo.addProduct(product);
                }else {
                    product.setAmount(pojo.getProductByName(product.getName()).getAmount());
                    pojo.updateProduct(product);
                }
            }
        } catch (IOException ioex) {
            throw new IOException("Error leyendo el fichero");
        }catch(NumberFormatException nf){
            throw new NumberFormatException("Error intentando obtener la cantidad del producto " + line);
        }
    }
    public File getFile() {
        return file;
    }
}
