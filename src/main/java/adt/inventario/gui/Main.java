package adt.inventario.gui;


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
    listar: mostrará todos los suministros que nos quedan.
    usar x suministro: actualizará la base de datos reduciendo en x el suministo pasado por parámetro. En caso de que la cantidad de suministros
    almacenados sea igual a x se deberá eliminar el suministro del inventario. En caso de que x sea mayor al número de suministros se deberá
    notificar como un error y no realizar ninguna acción.
    hay suministro: mostrar cuantos suministros nos quedan que contengan la cadena de texto pasada por parámetro.
    adquirir x suministro: insetará o actualizará el suministro con o en x unidades. Igual que cuando lo lee del fichero.
    salir: finaliza el programa.

Todos el programa ignorará mayúsculas y minúsculas. Se deberá permitir la ejecución ilimitada del programa solo parando al escribir la palabra salir
o parando el proceso de ejecución.
 */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String command;
        do {
            System.out.println("->");
            command = sc.nextLine();
        }while(command.equalsIgnoreCase("salir"));
    }
}