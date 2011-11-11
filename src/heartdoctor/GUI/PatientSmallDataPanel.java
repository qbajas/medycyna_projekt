/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * PatientDataPanel.java
 *
 * Created on 2011-11-11, 17:10:01
 */
package heartdoctor.GUI;

import heartdoctor.DataModel.PatientData;

/**
 *
 * @author michal
 */
public class PatientSmallDataPanel extends javax.swing.JPanel {

    /** Creates new form PatientDataPanel */
    public PatientSmallDataPanel() {
        initComponents();
    }

    public void setData(PatientData patient){
        pesel.setText(""+patient.getPesel());
        name.setText(patient.getName() + " "+ patient.getMiddleName());
        lastName.setText(patient.getLastName());
    }
    
   public PatientData getData(){
       PatientData data=new PatientData();
       data.setPesel( pesel.getText() );
       data.setName(name.getText());
       data.setLastName(lastName.getText());
       return data;
   }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lastName = new javax.swing.JLabel();
        pesel = new javax.swing.JLabel();
        name = new javax.swing.JLabel();

        setBackground(new java.awt.Color(102, 255, 204));

        jLabel1.setText("Name:");

        jLabel2.setText("Last name:");

        jLabel3.setText("Pesel:");

        lastName.setText("jLabel4");

        pesel.setText("jLabel4");

        name.setText("jLabel4");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(name)
                    .addComponent(pesel)
                    .addComponent(lastName))
                .addContainerGap(42, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(name))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(lastName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(pesel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel lastName;
    private javax.swing.JLabel name;
    private javax.swing.JLabel pesel;
    // End of variables declaration//GEN-END:variables
}
