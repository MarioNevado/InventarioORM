package adt.inventario.gui;


import adt.inventario.exceptions.UsedUnitsExceedException;
import adt.inventario.model.Product;
import adt.inventario.pojo.ProductPojo;
import adt.inventario.utils.CSVReader;
import adt.inventario.utils.HibernateUtil;

import java.io.File;
import java.io.IOException;
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

    public static void main(String[] args) {
        //TODO ocultar consultas internas en base a respuesta de Isma
        Scanner sc = new Scanner(System.in);
        String command;
        pojo = new ProductPojo();
        try {
            new CSVReader(new File("files/compra.csv"));
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
        HibernateUtil.shutdown();
    }

    private static void help(){
        System.out.println("\"listar\" -> Muestra suministros disponibles");
        System.out.println("\"usar x suministro\" -> Usar x unidades del suministro");
        System.out.println("\"hay suministro\" -> Muestra las unidades disponibles del suministro");
        System.out.println("\"adquirir suministro\" -> La base de datos adquiere una unidad de ese suministro");
        System.out.println("\"salir\" -> Salida controlada del programa");
    }
    private static void selectOption(String command) {
        try {
            if (command.equals("listar")) {
                for (Product p : pojo.list()){
                    System.out.println(p);
                }
            } else if (command.matches("^usar [0-9]+ [a-z]+[ a-z]*$")) {
                useProduct(command);
            } else if (command.matches("^hay [a-z]+[ a-z]*$")) {
                hasProduct(command);
            } else if (command.matches(("adquirir [a-z]+[ a-z]*$"))) {
                pojo.getProduct(command.split(" ", 2)[1]);
            } else {
                System.err.println("COMANDO INCORRECTO");
                help();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void hasProduct(String command){
        String product = command.split(" ", 2)[1];
        System.out.println("Unidades disponibles: " + pojo.getSupplies(new Product(product, 1)));
    }
    private static void useProduct(String command) {
        String product;
        int number;
        try {
            number = Integer.parseInt(command.split(" ", 3)[1]);
            product = command.split(" ", 3)[2];
            pojo.use(number, product);
        } catch (NumberFormatException nf) {
            System.err.println("Debe pasar un número como parámetro");
        } catch(UsedUnitsExceedException uue){
            System.err.println(uue.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}