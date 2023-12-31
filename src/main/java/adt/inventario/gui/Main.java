package adt.inventario.gui;


import adt.inventario.exceptions.IncorrectAcquiredUnitsException;
import adt.inventario.exceptions.UsedUnitsExceedException;
import adt.inventario.model.Product;
import adt.inventario.pojo.ProductPojo;
import adt.inventario.utils.CSVReader;
import adt.inventario.utils.HibernateUtil;

import java.io.*;
import java.util.Scanner;

/*

Dado el fichero adjunto con extensivo csv y separado por el carácter ";" con una lista de elementos que han sido comprados y que tiene un formato tal
que:
1;rollo papel higiénico
Realiza la carga de dicha información haciendo uso de un ORM, en una tabla que satisfaga los requisitos de almacenamiento y los requisitos de un ORM.
Es importante destacar que en caso de que se repita un elemento, no se tiene que volver a insertar, si no que se tiene que actualizar incrementando
 el número de unidades de dicho elemento. Por ejemplo:
1;rollo papel higiénico <==== se realizará un insert
7;rollo papel higiénico <==== se realizará un update
Al final se tendrá en la tabla que hay 8 unidades de papel higiénico.
Una vez realizada la carga de los datos se deberá acceder a ellos a través de palabras reservadas:
    - listar: mostrará todos los suministros que nos quedan.
    - usar x suministro: actualizará la base de datos reduciendo en x el suministo pasado por parámetro. En caso de que la cantidad de suministros
    almacenados sea igual a x se deberá eliminar el suministro del inventario. En caso de que x sea mayor al número de suministros se deberá
    notificar como un error y no realizar ninguna acción.
    - hay suministro: mostrar cuantos suministros nos quedan que contengan la cadena de texto pasada por parámetro.
    - adquirir x suministro: si lo tiene aumenta en una las unidades
    - salir: finaliza el programa.

Todos el programa ignorará mayúsculas y minúsculas. Se deberá permitir la ejecución ilimitada del programa solo parando al escribir la palabra salir
o parando el proceso de ejecución.
 */

public class Main {
    static ProductPojo pojo;
    static CSVReader csv;

    public static void main(String[] args) {
        //TODO ocultar consultas internas en base a respuesta de Isma
        Scanner sc = new Scanner(System.in);
        String command;
        final String path = "files/compra.csv";
        pojo = new ProductPojo();
        try {
            csv = new CSVReader(new File(path));
        } catch (IOException io) {
            io.printStackTrace();
        }
        do {
            System.out.print("-> ");
            command = sc.nextLine().toLowerCase();
            if (!command.equalsIgnoreCase("salir")) {
                selectOption(command);
            }
        } while (!command.equalsIgnoreCase("salir"));
        System.out.println("Hasta pronto!");
        HibernateUtil.shutdown();
    }

    private static void help() {
        String line;
        try(BufferedReader reader = new BufferedReader(new FileReader("files/help.csv"))){
            while((line = reader.readLine()) != null){
                line = line.replace(";", " -> ");
                System.out.println(line);
            }
        }catch(IOException io){
            System.err.println("Error en BufferedReader");
        }
    }

    private static void selectOption(String command) {
        try {
            if (command.equals("listar")) {
                for (Product p : pojo.list()) {
                    System.out.println(p);
                }
            } else if (command.matches("^usar [0-9]+ [a-z]+[ a-z]*$")) {
                useProduct(command);
            } else if (command.matches("^hay [a-z]+[ a-z]*$")) {
                hasProduct(command);
            } else if (command.matches(("^adquirir -?[0-9]* [a-z]+[ a-z]*$"))) {
                try {
                    pojo.getProduct(command.split(" ", 3)[2], Integer.parseInt(command.split(" ", 3)[1]));
                } catch (IncorrectAcquiredUnitsException iau) {
                    System.err.println(iau.getMessage());
                }
            } else if (command.equals("reiniciar")) {
                pojo.restart(csv);
            } else {
                System.err.println("COMANDO INCORRECTO");
                help();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void hasProduct(String command) {
        String product = command.split(" ", 2)[1];
        if (!pojo.hasProduct(product).isEmpty()) {
            System.out.println("Coincidencias: ");
            for (Product p : pojo.hasProduct(product)) {
                System.out.println(p);
            }
        } else System.err.println("No hay coincidencias");
    }

    private static void useProduct(String command) {
        String product;
        int number;
        try {
            number = Integer.parseInt(command.split(" ", 3)[1]);
            product = command.split(" ", 3)[2];
            pojo.use(number, pojo.getProductByName(product));
        } catch (NumberFormatException nf) {
            System.err.println("Debe pasar un número como parámetro");
        } catch (UsedUnitsExceedException uue) {
            System.err.println(uue.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}