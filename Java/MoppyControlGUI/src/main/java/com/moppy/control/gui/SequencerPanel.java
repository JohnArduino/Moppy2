package com.moppy.control.gui;

import com.moppy.control.GUIControlledPostProcessor;
import com.moppy.control.MoppyPreferences;
import com.moppy.core.midi.MoppyMIDIReceiverSender;
import com.moppy.core.midi.MoppyMIDISequencer;
import com.moppy.core.status.StatusConsumer;
import com.moppy.core.status.StatusUpdate;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 */
public class SequencerPanel extends JPanel implements StatusConsumer, ActionListener {

    private static final String TIME_CODE_FORMAT = "%d:%02d";
    private static final String SEQUENCE_PROGRESS = "SeqProgCmd";
    private static final String BTN_PLAY = "⏵";
    private static final String BTN_PAUSE = "⏸";

    private MoppyMIDIReceiverSender receiverSender;
    private MoppyMIDISequencer midiSequencer;
    private GUIControlledPostProcessor postProc;
    private final Timer sequenceProgressUpdateTimer;

    private Map<String, MidiDevice.Info> midiInDevices = new HashMap<>();
    private Map<String, MidiDevice.Info> midiOutDevices = new HashMap<>();
    private MidiDevice currentMidiInDevice = null;
    private MidiDevice currentMidiOutDevice = null;

    /**
     * Creates new form SequencerPanel
     */

    public SequencerPanel() {
        sequenceProgressUpdateTimer = new Timer(500, this);
        sequenceProgressUpdateTimer.setActionCommand(SEQUENCE_PROGRESS);

        initComponents();
        setControlsEnabled(false); // Leave these disabled until we've loaded a sequence
        refreshMidiDevices();
    }

     public void setReceiverSender(MoppyMIDIReceiverSender receiverSender) {
        this.receiverSender = receiverSender;
    }

    public void setMidiSequencer(MoppyMIDISequencer midiSequencer) {
        this.midiSequencer = midiSequencer;
        midiSequencer.setAutoReset(autoResetCB.isSelected());
    }

    public void setPostProcessor(GUIControlledPostProcessor postProc) {
        this.postProc = postProc;
    }

    private void refreshMidiDevices() {
        try{
            // Get all MIDI devices and add them to the devices maps based on capabilities
            for (MidiDevice.Info mdi : MidiSystem.getMidiDeviceInfo()) {
                if (MidiSystem.getMidiDevice(mdi).getMaxTransmitters() != 0) {
                    midiInDevices.put(mdi.getName(), mdi);
                }

                if (MidiSystem.getMidiDevice(mdi).getMaxReceivers() != 0){
                    midiOutDevices.put(mdi.getName(), mdi);
                }
            }
        }
        catch (Exception ex) {
            Logger.getLogger(SequencerPanel.class.getName()).log(Level.WARNING, "Exception getting list of MIDI devices-- MIDI In/Out will be unavailable", ex);
        }

        midiInCB.removeAllItems();
        midiOutCB.removeAllItems();

        midiInCB.addItem("None");
        midiOutCB.addItem("None");

        midiInDevices.keySet().forEach((key) -> midiInCB.addItem(key));
        midiOutDevices.keySet().forEach((key) -> midiOutCB.addItem(key));
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sequenceFileChooser = new javax.swing.JFileChooser();
        fileNameLabel = new javax.swing.JLabel();
        loadFileButton = new javax.swing.JButton();
        controlsPane = new javax.swing.JPanel();
        sequenceCurrentTimeLabel = new javax.swing.JLabel();
        sequenceSlider = new javax.swing.JSlider();
        sequenceTotalTimeLabel = new javax.swing.JLabel();
        stopButton = new javax.swing.JButton();
        playButton = new javax.swing.JButton();
        volumeSlider = new javax.swing.JSlider();
        volumeSliderLabel = new javax.swing.JLabel();
        volumeOverrideCB = new javax.swing.JCheckBox();
        midiInLabel = new javax.swing.JLabel();
        midiInCB = new javax.swing.JComboBox<>();
        midiOutLabel = new javax.swing.JLabel();
        midiOutCB = new javax.swing.JComboBox<>();
        autoResetCB = new javax.swing.JCheckBox();
        autoResetCB.setSelected(MoppyPreferences.getConfiguration().isAutoReset());

        sequenceFileChooser.setCurrentDirectory(new File(MoppyPreferences.getConfiguration().getFileLoadDirectory()));
        sequenceFileChooser.setDialogTitle("Select MIDI File");
        sequenceFileChooser.setFileFilter(new FileNameExtensionFilter("MIDI Files", "mid"));

        setMinimumSize(new java.awt.Dimension(400, 200));
        setName("sequencerPanel"); // NOI18N

        fileNameLabel.setText("No file loaded...");
        fileNameLabel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        loadFileButton.setText("Load File");
        loadFileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadFileButtonActionPerformed(evt);
            }
        });

        controlsPane.setMinimumSize(new java.awt.Dimension(400, 149));

        sequenceCurrentTimeLabel.setText("00:00");

        sequenceSlider.setMajorTickSpacing(60);
        sequenceSlider.setMaximum(120);
        sequenceSlider.setMinorTickSpacing(15);
        sequenceSlider.setPaintTicks(true);
        sequenceSlider.setValue(0);
        sequenceSlider.setPreferredSize(new java.awt.Dimension(200, 32));
        sequenceSlider.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                sequenceSliderMouseDragged(evt);
            }
        });
        sequenceSlider.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                sequenceSliderMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                sequenceSliderMouseReleased(evt);
            }
        });

        sequenceTotalTimeLabel.setText("00:00");

        stopButton.setText("⏹");
        stopButton.setToolTipText("Stop / Reset instruments");
        stopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopButtonActionPerformed(evt);
            }
        });

        playButton.setFont(playButton.getFont().deriveFont(playButton.getFont().getSize()+6f));
        playButton.setText("⏵");
        playButton.setToolTipText("Play / Pause sequence playback");
        playButton.setMargin(new java.awt.Insets(2, 14, 5, 14));
        playButton.setMaximumSize(new java.awt.Dimension(49, 23));
        playButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playButtonActionPerformed(evt);
            }
        });

        volumeSlider.setValue(100);
        volumeSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                volumeSliderStateChanged(evt);
            }
        });

        volumeSliderLabel.setText("100% Volume");

        volumeOverrideCB.setText("Overide MIDI Volume");
        volumeOverrideCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                volumeOverrideCBActionPerformed(evt);
            }
        });

        midiInLabel.setText("MIDI In:");
        midiInLabel.setToolTipText("MIDI device to receive events from");

        midiInCB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        midiInCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                midiInCBActionPerformed(evt);
            }
        });

        midiOutLabel.setText("MIDI Out:");
        midiOutLabel.setToolTipText("MIDI device to send all raw MIDI events to.");

        midiOutCB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        midiOutCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                midiOutCBActionPerformed(evt);
            }
        });

        autoResetCB.setText("Auto Reset");
        autoResetCB.setToolTipText("Resets the instruments when a sequence ends (when unchecked, use the Stop button to reset)");
        autoResetCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                autoResetCBActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout controlsPaneLayout = new javax.swing.GroupLayout(controlsPane);
        controlsPane.setLayout(controlsPaneLayout);
        controlsPaneLayout.setHorizontalGroup(
            controlsPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(controlsPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(controlsPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(controlsPaneLayout.createSequentialGroup()
                        .addComponent(sequenceCurrentTimeLabel)
                        .addGap(175, 362, Short.MAX_VALUE))
                    .addGroup(controlsPaneLayout.createSequentialGroup()
                        .addGroup(controlsPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(controlsPaneLayout.createSequentialGroup()
                                .addComponent(stopButton, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(controlsPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, controlsPaneLayout.createSequentialGroup()
                                        .addComponent(sequenceSlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(sequenceTotalTimeLabel))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, controlsPaneLayout.createSequentialGroup()
                                        .addComponent(playButton, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(volumeSliderLabel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(volumeSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, controlsPaneLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(volumeOverrideCB))
                            .addGroup(controlsPaneLayout.createSequentialGroup()
                                .addGroup(controlsPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(midiInLabel)
                                    .addComponent(midiOutLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(controlsPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(midiOutCB, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(midiInCB, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(controlsPaneLayout.createSequentialGroup()
                        .addComponent(autoResetCB)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        controlsPaneLayout.setVerticalGroup(
            controlsPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(controlsPaneLayout.createSequentialGroup()
                .addGroup(controlsPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(controlsPaneLayout.createSequentialGroup()
                        .addComponent(sequenceTotalTimeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(volumeSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(controlsPaneLayout.createSequentialGroup()
                        .addComponent(sequenceCurrentTimeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(controlsPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(stopButton, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(playButton, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(controlsPaneLayout.createSequentialGroup()
                        .addComponent(sequenceSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(volumeSliderLabel)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(volumeOverrideCB)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(controlsPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(midiInLabel)
                    .addComponent(midiInCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(controlsPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(midiOutLabel)
                    .addComponent(midiOutCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(autoResetCB)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fileNameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(loadFileButton)
                .addContainerGap())
            .addComponent(controlsPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(loadFileButton)
                    .addComponent(fileNameLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(controlsPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void loadFileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadFileButtonActionPerformed
        if (JFileChooser.APPROVE_OPTION == sequenceFileChooser.showOpenDialog(this)) {
            try {
                File selectedFile = sequenceFileChooser.getSelectedFile();
                midiSequencer.loadSequence(selectedFile);
                fileNameLabel.setText(selectedFile.getName());
                MoppyPreferences.getConfiguration().setFileLoadDirectory(selectedFile.getParentFile().getAbsolutePath());
                MoppyPreferences.saveConfiguration();
            } catch (IOException | InvalidMidiDataException ex) {
                Logger.getLogger(SequencerPanel.class.getName()).log(Level.WARNING, null, ex);
            }
        }
    }//GEN-LAST:event_loadFileButtonActionPerformed

    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopButtonActionPerformed
        midiSequencer.stop();
    }//GEN-LAST:event_stopButtonActionPerformed

    private void playButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playButtonActionPerformed
        if (midiSequencer.isPlaying()) {
            midiSequencer.pause();
        }  else {
            midiSequencer.play();
        }
    }//GEN-LAST:event_playButtonActionPerformed

    private void sequenceSliderMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sequenceSliderMouseDragged
        midiSequencer.setSecondsPosition(sequenceSlider.getValue());
        Duration length = Duration.ofSeconds(midiSequencer.getSecondsPosition());
        sequenceCurrentTimeLabel.setText(String.format(TIME_CODE_FORMAT, length.toMinutes(), length.getSeconds()));
    }//GEN-LAST:event_sequenceSliderMouseDragged

    private void sequenceSliderMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sequenceSliderMousePressed
        midiSequencer.pause();
    }//GEN-LAST:event_sequenceSliderMousePressed

    private void sequenceSliderMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sequenceSliderMouseReleased
        sequenceSliderMouseDragged(evt); // Update position
        midiSequencer.play();
    }//GEN-LAST:event_sequenceSliderMouseReleased

    private void volumeSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_volumeSliderStateChanged
        volumeSliderLabel.setText(String.format("%s%% Volume", volumeSlider.getValue()));
        postProc.setVelocityMultiplier(volumeSlider.getValue()/100.0);
    }//GEN-LAST:event_volumeSliderStateChanged

    private void volumeOverrideCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_volumeOverrideCBActionPerformed
        postProc.setOverrideVelocity(volumeOverrideCB.isSelected());
    }//GEN-LAST:event_volumeOverrideCBActionPerformed

    private void midiOutCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_midiOutCBActionPerformed
        if (receiverSender == null) {
            return; // We can't do anything if the receiverSender hasn't been initialized / set yet.
        }

        String selectedName = midiOutCB.getSelectedItem().toString();

        if (midiOutDevices.containsKey(selectedName)) {
            try {
                currentMidiOutDevice = MidiSystem.getMidiDevice(midiOutDevices.get(selectedName));
                currentMidiOutDevice.open();
                receiverSender.setMidiThru(MidiSystem.getMidiDevice(midiOutDevices.get(selectedName)).getReceiver());
            } catch (MidiUnavailableException ex) {
                Logger.getLogger(SequencerPanel.class.getName()).log(Level.SEVERE, null, ex);
                midiOutCB.setSelectedIndex(0); // On exception, set menu back to "None"
            }
        } else {
            receiverSender.setMidiThru(null); // Disable thru sending
        }
    }//GEN-LAST:event_midiOutCBActionPerformed

    private void midiInCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_midiInCBActionPerformed
        // If we'd previously selected a MIDI In device, remove us as a receiver so we're not getting
        // events from it.
        if (currentMidiInDevice != null) {
            currentMidiInDevice.close();
        }

        if (receiverSender == null) {
            return; // We can't do anything if the receiverSender hasn't been initialized / set yet.
        }

        String selectedName = midiInCB.getSelectedItem().toString();

        if (midiInDevices.containsKey(selectedName)) {
            try {
                // Set currentMidiInDevice so we can remove ourselves as its receiver later
                currentMidiInDevice = MidiSystem.getMidiDevice(midiInDevices.get(selectedName));
                currentMidiInDevice.open();
                currentMidiInDevice.getTransmitter().setReceiver(receiverSender);
            } catch (MidiUnavailableException ex) {
                Logger.getLogger(SequencerPanel.class.getName()).log(Level.SEVERE, null, ex);
                midiInCB.setSelectedIndex(0); // On exception, set menu back to "None"
            }
        }
    }//GEN-LAST:event_midiInCBActionPerformed

    private void autoResetCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoResetCBActionPerformed
        if (midiSequencer != null) {
            midiSequencer.setAutoReset(autoResetCB.isSelected());
        }
        MoppyPreferences.getConfiguration().setAutoReset(autoResetCB.isSelected());
    }//GEN-LAST:event_autoResetCBActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox autoResetCB;
    private javax.swing.JPanel controlsPane;
    private javax.swing.JLabel fileNameLabel;
    private javax.swing.JButton loadFileButton;
    private javax.swing.JComboBox<String> midiInCB;
    private javax.swing.JLabel midiInLabel;
    private javax.swing.JComboBox<String> midiOutCB;
    private javax.swing.JLabel midiOutLabel;
    private javax.swing.JButton playButton;
    private javax.swing.JLabel sequenceCurrentTimeLabel;
    private javax.swing.JFileChooser sequenceFileChooser;
    private javax.swing.JSlider sequenceSlider;
    private javax.swing.JLabel sequenceTotalTimeLabel;
    private javax.swing.JButton stopButton;
    private javax.swing.JCheckBox volumeOverrideCB;
    private javax.swing.JSlider volumeSlider;
    private javax.swing.JLabel volumeSliderLabel;
    // End of variables declaration//GEN-END:variables

    @Override
    public void receiveUpdate(StatusUpdate update) {
        switch (update.getType()) {
            case SEQUENCE_LOAD:
                Duration length = Duration.ofSeconds(midiSequencer.getSecondsLength());
                sequenceSlider.setMaximum((int) length.getSeconds());
                sequenceTotalTimeLabel.setText(String.format(TIME_CODE_FORMAT, length.toMinutes(), length.getSeconds()%60));
                setControlsEnabled(true);
                break;
            case SEQUENCE_START:
                playButton.setText(BTN_PAUSE);
                sequenceProgressUpdateTimer.start();
                break;
            case SEQUENCE_END:
                sequenceSlider.setValue(0);
                sequenceCurrentTimeLabel.setText(String.format(TIME_CODE_FORMAT, 0, 0));
            case SEQUENCE_PAUSE:
                playButton.setText(BTN_PLAY);
                sequenceProgressUpdateTimer.stop();
                break;
        }
    }

    private void setControlsEnabled(boolean enabled) {
        Arrays.stream(controlsPane.getComponents()).forEach(c -> c.setEnabled(enabled));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (SEQUENCE_PROGRESS.equals(e.getActionCommand())) {
            Duration length = Duration.ofSeconds(midiSequencer.getSecondsPosition());
            sequenceSlider.setValue((int) length.getSeconds());
            sequenceCurrentTimeLabel.setText(String.format(TIME_CODE_FORMAT, length.toMinutes(), length.getSeconds()%60));
        }
    }
}
