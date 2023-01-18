package GUI;

import Matrices.MatrizMaster;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import main.SumoMain;

public class SumoMainGuiPrueba extends javax.swing.JFrame {

    public SumoMainGuiPrueba() {
        this.setTitle("Sistema de repartos MAS");
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFileChooser1 = new javax.swing.JFileChooser();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        enviar = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtCapacidadBuses = new javax.swing.JTextField();
        txtVelocidadRepartidoresBicicleta = new javax.swing.JTextField();
        txtVelocidadBuses = new javax.swing.JTextField();
        txtVelocidadVehiculos = new javax.swing.JTextField();
        csvRepartidoresSeleccionar = new javax.swing.JButton();
        csvVehiculosSeleccionar = new javax.swing.JButton();
        csvBusesSeleccionar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtSalida = new javax.swing.JTextArea();
        txtVelocidadRepartidoresMoto = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        cargarMatrices = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        jLabel1.setText("Autos");

        jLabel2.setText("Velocidad (m/s):");

        jLabel3.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        jLabel3.setText("Buses");

        jLabel4.setText("Velocidad (m/s):");

        jLabel5.setText("Capacidad:");

        jLabel6.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        jLabel6.setText("Repartidores");

        jLabel7.setText("Velocidad Bicicleta (m/s):");

        enviar.setText("Enviar");
        enviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enviarActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        jLabel9.setText("Matriz Buses:");

        jLabel10.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        jLabel10.setText("Matriz Repartidores:");

        jLabel11.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        jLabel11.setText("Matriz Autos:");

        txtCapacidadBuses.setText("45");

        txtVelocidadRepartidoresBicicleta.setText("8");

        txtVelocidadBuses.setText("20");

        txtVelocidadVehiculos.setText("25");

        csvRepartidoresSeleccionar.setText("Seleccionar");
        csvRepartidoresSeleccionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                csvRepartidoresSeleccionarActionPerformed(evt);
            }
        });

        csvVehiculosSeleccionar.setText("Seleccionar");
        csvVehiculosSeleccionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                csvVehiculosSeleccionarActionPerformed(evt);
            }
        });

        csvBusesSeleccionar.setText("Seleccionar");
        csvBusesSeleccionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                csvBusesSeleccionarActionPerformed(evt);
            }
        });

        txtSalida.setColumns(20);
        txtSalida.setRows(5);
        jScrollPane1.setViewportView(txtSalida);

        txtVelocidadRepartidoresMoto.setText("20");

        jLabel8.setText("Velocidad Moto  (m/s):");

        cargarMatrices.setText("Cargar Matrices");
        cargarMatrices.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cargarMatricesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(53, 53, 53)
                        .addComponent(csvBusesSeleccionar))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(11, 11, 11)
                        .addComponent(csvRepartidoresSeleccionar)
                        .addGap(13, 13, 13)
                        .addComponent(cargarMatrices, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addGap(57, 57, 57)
                        .addComponent(csvVehiculosSeleccionar))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel2)
                        .addGap(16, 16, 16)
                        .addComponent(txtVelocidadVehiculos, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel3))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel4)
                        .addGap(16, 16, 16)
                        .addComponent(txtVelocidadBuses, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel5)
                        .addGap(32, 32, 32)
                        .addComponent(txtCapacidadBuses, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel6))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel7)
                        .addGap(12, 12, 12)
                        .addComponent(txtVelocidadRepartidoresBicicleta, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel8)
                        .addGap(29, 29, 29)
                        .addComponent(txtVelocidadRepartidoresMoto, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 534, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addGap(140, 140, 140)
                .addComponent(enviar))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(csvBusesSeleccionar, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(csvRepartidoresSeleccionar, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cargarMatrices))
                        .addGap(7, 7, 7)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(csvVehiculosSeleccionar, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addComponent(jLabel1)
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(txtVelocidadVehiculos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(7, 7, 7)
                        .addComponent(jLabel3)
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(txtVelocidadBuses, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(7, 7, 7)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(txtCapacidadBuses, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(7, 7, 7)
                        .addComponent(jLabel6)
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(txtVelocidadRepartidoresBicicleta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(7, 7, 7)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(txtVelocidadRepartidoresMoto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addComponent(enviar))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void enviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enviarActionPerformed
        // TODO add your handling code here:
        txtSalida.setEditable(false);
        txtSalida.setText("");
        SynchronizedByteArrayOutputStreamWrapper rawout = new SynchronizedByteArrayOutputStreamWrapper();
        Thread consoleThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                String pendingConsoleOutput = new String(rawout.readEmpty());
                txtSalida.append(pendingConsoleOutput);
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        });
        consoleThread.start();
        // Set new stream for System.out
        System.setOut(new PrintStream(rawout, true));
        
        double vAutos,vBuses,vBici,vMoto;
        int cBuses;
        try{
            vAutos=Double.parseDouble(txtVelocidadVehiculos.getText());
            vBuses=Double.parseDouble(txtVelocidadBuses.getText());
            cBuses=Integer.parseInt(txtCapacidadBuses.getText());
            vBici=Double.parseDouble(txtVelocidadRepartidoresBicicleta.getText());
            vMoto=Double.parseDouble(txtVelocidadRepartidoresMoto.getText());
            if(vAutos<=0 ||vBuses<=0||cBuses<=0||vBici<=0||vMoto<=0) throw new NumberFormatException();
            SumoMain.getInstance().iniciar(this.csvBuses,this.csvRepartidores,this.csvVehiculos,this.mat,vAutos,vBuses,cBuses,vBici,vMoto);
        }
        catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null,"Ingrese valores de velocidad numéricos positivos", "Error en el ingreso de datos", JOptionPane.ERROR_MESSAGE);
        } 
    }//GEN-LAST:event_enviarActionPerformed

    private void csvRepartidoresSeleccionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_csvRepartidoresSeleccionarActionPerformed
        // TODO add your handling code here:
        JFileChooser jFileChooser = new JFileChooser();
        FileNameExtensionFilter filtrado = new FileNameExtensionFilter("CSV", "csv");
        jFileChooser.setFileFilter(filtrado);
        
        int respuesta = jFileChooser.showOpenDialog(this);
        
        if (respuesta == JFileChooser.APPROVE_OPTION) {
            csvRepartidores=jFileChooser.getSelectedFile().getPath();
            
        }
    }//GEN-LAST:event_csvRepartidoresSeleccionarActionPerformed

    private void csvBusesSeleccionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_csvBusesSeleccionarActionPerformed
        // TODO add your handling code here:
        JFileChooser jFileChooser = new JFileChooser();
        FileNameExtensionFilter filtrado = new FileNameExtensionFilter("CSV", "csv");
        jFileChooser.setFileFilter(filtrado);
        
        int respuesta = jFileChooser.showOpenDialog(this);
        
        if (respuesta == JFileChooser.APPROVE_OPTION) {
            csvBuses=jFileChooser.getSelectedFile().getPath();
            
        }
    }//GEN-LAST:event_csvBusesSeleccionarActionPerformed

    private void csvVehiculosSeleccionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_csvVehiculosSeleccionarActionPerformed
        // TODO add your handling code here:
        JFileChooser jFileChooser = new JFileChooser();
        FileNameExtensionFilter filtrado = new FileNameExtensionFilter("CSV", "csv");
        jFileChooser.setFileFilter(filtrado);
        
        int respuesta = jFileChooser.showOpenDialog(this);
        
        if (respuesta == JFileChooser.APPROVE_OPTION) {
            csvVehiculos=jFileChooser.getSelectedFile().getPath();
        }
    }//GEN-LAST:event_csvVehiculosSeleccionarActionPerformed

    private void cargarMatricesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cargarMatricesActionPerformed
        // TODO add your handling code here:
        this.csvBuses="resources/MatricesCSV/matriz.csv";
        this.csvRepartidores="resources/MatricesCSV/matrizRepartidores.csv";
        this.csvVehiculos="resources/MatricesCSV/matrizVehiculos.csv";
        String tipoRepartidor="";
        mat=new MatrizMaster(csvBuses, csvRepartidores, csvVehiculos);
        for (int i=0;i<mat.getRepartidores().size();i++) {
            tipoRepartidor=JOptionPane.showInputDialog("El repartidor: " + mat.getRepartidores().get(i).getID() + " sera de tipo Moto(M), o Bisicleta(B)");
            mat.getRepartidores().get(i).setTipoRepartidor(tipoRepartidor);
        }
    }//GEN-LAST:event_cargarMatricesActionPerformed

    private class SynchronizedByteArrayOutputStreamWrapper
            extends OutputStream {
        // The console will be synchronized through a monitor.
        // WARNING! This could delay the code trying to write to the console!
        private final Object monitor = new Object();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        @Override
        public void write(int data) throws IOException {
            synchronized (monitor) {
                byteArrayOutputStream.write(data);
            }
        }

        public byte[] readEmpty() {
            byte[] bufferContent;  
            synchronized(monitor) {
                bufferContent = byteArrayOutputStream.toByteArray();
                byteArrayOutputStream.reset();
            }
            return bufferContent;
        }
    }
    
    private String csvVehiculos;
    private String csvBuses;
    private String csvRepartidores;
    private MatrizMaster mat;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cargarMatrices;
    private javax.swing.JButton csvBusesSeleccionar;
    private javax.swing.JButton csvRepartidoresSeleccionar;
    private javax.swing.JButton csvVehiculosSeleccionar;
    private javax.swing.JButton enviar;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtCapacidadBuses;
    public javax.swing.JTextArea txtSalida;
    private javax.swing.JTextField txtVelocidadBuses;
    private javax.swing.JTextField txtVelocidadRepartidoresBicicleta;
    private javax.swing.JTextField txtVelocidadRepartidoresMoto;
    private javax.swing.JTextField txtVelocidadVehiculos;
    // End of variables declaration//GEN-END:variables
}
