package GUI;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import main.SumoMain;

public class SumoMainGui extends javax.swing.JFrame {

    public SumoMainGui() {
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        jLabel1.setText("Autos");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, -1, -1));

        jLabel2.setText("Velocidad (m/s):");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 130, -1, -1));

        jLabel3.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        jLabel3.setText("Buses");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 160, -1, -1));

        jLabel4.setText("Velocidad (m/s):");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 190, -1, -1));

        jLabel5.setText("Capacidad:");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 220, -1, -1));

        jLabel6.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        jLabel6.setText("Repartidores");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 250, -1, -1));

        jLabel7.setText("Velocidad Bicicleta (m/s):");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 280, -1, -1));

        enviar.setText("Enviar");
        enviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enviarActionPerformed(evt);
            }
        });
        getContentPane().add(enviar, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 350, -1, -1));

        jLabel9.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        jLabel9.setText("Matriz Buses:");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        jLabel10.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        jLabel10.setText("Matriz Repartidores:");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, -1, -1));

        jLabel11.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        jLabel11.setText("Matriz Autos:");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, -1, -1));

        txtCapacidadBuses.setText("45");
        getContentPane().add(txtCapacidadBuses, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 220, 139, -1));

        txtVelocidadRepartidoresBicicleta.setText("8");
        getContentPane().add(txtVelocidadRepartidoresBicicleta, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 280, 139, -1));

        txtVelocidadBuses.setText("20");
        getContentPane().add(txtVelocidadBuses, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 190, 139, -1));

        txtVelocidadVehiculos.setText("25");
        getContentPane().add(txtVelocidadVehiculos, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 130, 139, -1));

        csvRepartidoresSeleccionar.setText("Seleccionar");
        csvRepartidoresSeleccionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                csvRepartidoresSeleccionarActionPerformed(evt);
            }
        });
        getContentPane().add(csvRepartidoresSeleccionar, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 40, -1, 20));

        csvVehiculosSeleccionar.setText("Seleccionar");
        csvVehiculosSeleccionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                csvVehiculosSeleccionarActionPerformed(evt);
            }
        });
        getContentPane().add(csvVehiculosSeleccionar, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 70, -1, 20));

        csvBusesSeleccionar.setText("Seleccionar");
        csvBusesSeleccionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                csvBusesSeleccionarActionPerformed(evt);
            }
        });
        getContentPane().add(csvBusesSeleccionar, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 10, -1, 20));

        txtSalida.setColumns(20);
        txtSalida.setRows(5);
        jScrollPane1.setViewportView(txtSalida);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 20, 534, 330));

        txtVelocidadRepartidoresMoto.setText("20");
        getContentPane().add(txtVelocidadRepartidoresMoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 310, 139, -1));

        jLabel8.setText("Velocidad Moto  (m/s):");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 310, -1, -1));

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
            SumoMain.getInstance().iniciar("resources/MatricesCSV/matriz.csv","resources/MatricesCSV/matrizRepartidores.csv","resources/MatricesCSV/matrizVehiculos.csv",vAutos,vBuses,cBuses,vBici,vMoto);
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
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
