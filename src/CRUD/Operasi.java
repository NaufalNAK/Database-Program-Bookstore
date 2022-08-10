package CRUD;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Operasi {

    public static void updateData() throws IOException {
        // kita ambil database original
        File database = new File("database.txt");
        FileReader fileInput = new FileReader(database);
        BufferedReader bufferedInput = new BufferedReader(fileInput);

        // kita buat database sementara
        File tempDB = new File("tempDB.txt");
        FileWriter fileOutput = new FileWriter(tempDB);
        BufferedWriter bufferedOutput = new BufferedWriter(fileOutput);

        // tampilkan data
        System.out.println("List Buku");
        tampilkanData();

        // ambil user input / pilihan data
        Scanner terminalInput = new Scanner(System.in);
        System.out.print("\nMasukan nomor buku yang akan diupdate: ");
        int updateNum = terminalInput.nextInt();

        // tampilkan data yang ingin diupdate

        String data = bufferedInput.readLine();
        int entryCounts = 0;

        while (data != null){
            entryCounts++;

            StringTokenizer st = new StringTokenizer(data,",");

            // tampilkan entrycounts == updateNum
            if (updateNum == entryCounts){
                System.out.println("\nData yang ingin anda update adalah:");
                System.out.println("---------------------------------------");
                System.out.println("Referensi           : " + st.nextToken());
                System.out.println("Tahun               : " + st.nextToken());
                System.out.println("Penulis             : " + st.nextToken());
                System.out.println("Penerbit            : " + st.nextToken());
                System.out.println("Judul               : " + st.nextToken());

                // update data

                // mengambil input dari user

                String[] fieldData = {"tahun","penulis","penerbit","judul"};
                String[] tempData = new String[4];

                st = new StringTokenizer(data,",");
                String originalData = st.nextToken();

                for(int i=0; i < fieldData.length ; i++) {
                    boolean isUpdate = Ultility.getYesorNo("Apakah anda ingin merubah " + fieldData[i]);
                    originalData = st.nextToken();
                    if (isUpdate){
                        //user input

                        if (fieldData[i].equalsIgnoreCase("tahun")){
                            System.out.print("Masukan tahun terbit, format=(YYYY): ");
                            tempData[i] = Ultility.ambilTahun();
                        } else {
                            terminalInput = new Scanner(System.in);
                            System.out.print("\nMasukan " + fieldData[i] + " baru: ");
                            tempData[i] = terminalInput.nextLine();
                        }

                    } else {
                        tempData[i] = originalData;
                    }
                }

                // tampilkan data baru ke layar
                st = new StringTokenizer(data,",");
                st.nextToken();
                System.out.println("\nData baru anda adalah ");
                System.out.println("---------------------------------------");
                System.out.println("Tahun               : " + st.nextToken() + " --> " + tempData[0]);
                System.out.println("Penulis             : " + st.nextToken() + " --> " + tempData[1]);
                System.out.println("Penerbit            : " + st.nextToken() + " --> " + tempData[2]);
                System.out.println("Judul               : " + st.nextToken() + " --> " + tempData[3]);


                boolean isUpdate = Ultility.getYesorNo("Apakah anda yakin ingin mengupdate data tersebut");

                if (isUpdate){

                    // cek data baru di database
                    boolean isExist = Ultility.cekBukuDiDatabase(tempData,false);

                    if(isExist){
                        System.err.println("Data buku sudah ada di database, proses update dibatalkan, \nsilahkan delete data yang bersangkutan");
                        // copy data
                        bufferedOutput.write(data);

                    } else {

                        // format data baru kedalam database
                        String tahun = tempData[0];
                        String penulis = tempData[1];
                        String penerbit = tempData[2];
                        String judul = tempData[3];

                        // kita bikin primary key
                        long nomorEntry = Ultility.ambilEntryPerTahun(penulis, tahun) + 1;

                        String punulisTanpaSpasi = penulis.replaceAll("\\s+","");
                        String primaryKey = punulisTanpaSpasi+"_"+tahun+"_"+nomorEntry;

                        // tulis data ke database
                        bufferedOutput.write(primaryKey + "," + tahun + ","+ penulis +"," + penerbit + ","+judul);
                    }
                } else {
                    // copy data
                    bufferedOutput.write(data);
                }
            } else {
                // copy data
                bufferedOutput.write(data);
            }
            bufferedOutput.newLine();

            data = bufferedInput.readLine();
        }

        // menulis data ke file
        bufferedOutput.flush();

        bufferedInput.close();
        bufferedOutput.close();
        fileInput.close();
        fileOutput.close();

        // menjalankan method
        System.gc();

        // delete original file
        database.delete();
        // rename file sementara ke database
        tempDB.renameTo(database);

    }

    public static void deleteBuku() throws IOException{
        // ambil DB original
        File database = new File("database.txt");
        FileReader fileInput = new FileReader(database);
        BufferedReader bufferedInput = new BufferedReader(fileInput);

        // buat DB sementara
        File tempDB = new File("tempDB.txt");
        FileWriter fileOutput = new FileWriter(tempDB);
        BufferedWriter bufferedOutput = new BufferedWriter(fileOutput);

        // tampilkan data
        System.out.println("List Buku");
        tampilkanData();

        // ambil user input untuk mendelete data
        Scanner termianlInput = new Scanner(System.in);
        System.out.print("\nMasukkan nomor buku yang akan dihapus : ");
        int deleteNum = termianlInput.nextInt();

        // looping untuk membaca data baris dan skip data yang didelete
        boolean isFound =  false;
        int entryCounts = 0;

        String data = bufferedInput.readLine();

        while( data != null){
            entryCounts++;
            boolean isDelete = false;

            StringTokenizer stringToken = new StringTokenizer(data, ",");

            // tampilkan data yang ingin dihapus
            if (deleteNum == entryCounts){
                System.out.println("\nData yang ingin anda hapus adalah :");
                System.out.println("-------------------------------------");
                System.out.println("Referensi   :"+stringToken.nextToken());
                System.out.println("Tahun       :"+stringToken.nextToken());
                System.out.println("Penulis     :"+stringToken.nextToken());
                System.out.println("Penerbit    :"+stringToken.nextToken());
                System.out.println("Judul       :"+stringToken.nextToken());

                isDelete = Ultility.getYesorNo("Apakah anda yakin untuk menghapus ?");
                isFound = true;
            }
            if (isDelete){
                // pindahkan data dari original untuk semetara
                System.out.println("Data berhasil dihapus");
            }else{
                // pindahkan data dari original untuk sementara
                bufferedOutput.write(data);
                bufferedOutput.newLine();
            }
            data = bufferedInput.readLine();
        }
        if (!isFound){
            System.err.println("Buku tidak ditemukan");
        }
        // menulis data ke file
        bufferedOutput.flush();

        bufferedInput.close();
        bufferedOutput.close();
        fileInput.close();
        fileOutput.close();

        // menjalankan method
        System.gc();

        // delete original file
        database.delete();
        // rename file sementara ke database
        tempDB.renameTo(database);

    }

    public static void tampilkanData() throws IOException{

        FileReader fileInput;
        BufferedReader bufferInput;

        try {
            fileInput = new FileReader("database.txt");
            bufferInput = new BufferedReader(fileInput);
        } catch (Exception e){
            System.err.println("Database Tidak ditemukan");
            System.err.println("Silahkan tambah data terlebih dahulu");
            tambahData();
            return;
        }


        System.out.println("\n| No |\tTahun |\tPenulis                |\tPenerbit               |\tJudul Buku");
        System.out.println("----------------------------------------------------------------------------------------------------------");

        String data = bufferInput.readLine();
        int nomorData = 0;
        while(data != null) {
            nomorData++;

            StringTokenizer stringToken = new StringTokenizer(data, ",");

            stringToken.nextToken();
            System.out.printf("| %2d ", nomorData);
            System.out.printf("|\t%4s  ", stringToken.nextToken());
            System.out.printf("|\t%-20s   ", stringToken.nextToken());
            System.out.printf("|\t%-20s   ", stringToken.nextToken());
            System.out.printf("|\t%s   ", stringToken.nextToken());
            System.out.print("\n");

            data = bufferInput.readLine();
        }

        System.out.println("----------------------------------------------------------------------------------------------------------");
    }

    public static void cariData() throws IOException{

        // membaca database ada atau tidak

        try {
            File file = new File("database.txt");
        } catch (Exception e){
            System.err.println("Database Tidak ditemukan");
            System.err.println("Silahkan tambah data terlebih dahulu");
            tambahData();
            return;
        }

        // kita ambil keyword dari user

        Scanner terminalInput = new Scanner(System.in);
        System.out.print("Masukan kata kunci untuk mencari buku : ");
        String cariString = terminalInput.nextLine();
        String[] keywords = cariString.split("\\s+");

        // kita cek keyword di database
        Ultility.cekBukuDiDatabase(keywords,true);

    }

    public static void tambahData() throws IOException{

        FileWriter fileOutput = new FileWriter("database.txt",true);
        BufferedWriter bufferOutput = new BufferedWriter(fileOutput);

        // mengambil input dari user
        Scanner terminalInput = new Scanner(System.in);
        String penulis, judul, penerbit, tahun;

        System.out.print("Masukan nama penulis : ");
        penulis = terminalInput.nextLine();
        System.out.print("Masukan judul buku : ");
        judul = terminalInput.nextLine();
        System.out.print("Masukan nama penerbit : ");
        penerbit = terminalInput.nextLine();
        System.out.print("Masukan tahun terbit, format=(YYYY) : ");
        tahun = Ultility.ambilTahun();

        // cek buku di database

        String[] keywords = {tahun+","+penulis+","+penerbit+","+judul};
        System.out.println(Arrays.toString(keywords));

        boolean isExist = Ultility.cekBukuDiDatabase(keywords,false);

        // menulis buku di databse
        if (!isExist){
            // contoh : radityadika_2005_1,2005,Raditya Dika,Gagasmedia,Kambing Jantan
            System.out.println(Ultility.ambilEntryPerTahun(penulis, tahun));
            long nomorEntry = Ultility.ambilEntryPerTahun(penulis, tahun) + 1;

            String punulisTanpaSpasi = penulis.replaceAll("\\s+","");
            String primaryKey = punulisTanpaSpasi+"_"+tahun+"_"+nomorEntry;
            System.out.println("\nData yang akan anda masukan adalah");
            System.out.println("----------------------------------------");
            System.out.println("Primary key  : " + primaryKey);
            System.out.println("Tahun terbit : " + tahun);
            System.out.println("Penulis      : " + penulis);
            System.out.println("Judul        : " + judul);
            System.out.println("Penerbit     : " + penerbit);

            boolean isTambah = Ultility.getYesorNo("Apakah akan ingin menambah data tersebut? ");

            if(isTambah){
                bufferOutput.write(primaryKey + "," + tahun + ","+ penulis +"," + penerbit + ","+judul);
                bufferOutput.newLine();
                bufferOutput.flush();
            }

        } else {
            System.out.println("Buku yang anda akan masukan sudah tersedia di database dengan data berikut:");
            Ultility.cekBukuDiDatabase(keywords,true);
        }

        bufferOutput.close();
    }
}
