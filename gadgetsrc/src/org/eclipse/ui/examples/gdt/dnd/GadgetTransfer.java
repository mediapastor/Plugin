package org.eclipse.ui.examples.gdt.dnd;

import java.io.*;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.ui.examples.gdt.Gadget;
/**
 * Class for serializing gadgets to/from a byte array
 */
public class GadgetTransfer extends ByteArrayTransfer {
   private static GadgetTransfer instance = new GadgetTransfer();
   private static final String TYPE_NAME = "gadget-transfer-format";
   private static final int TYPEID = registerType(TYPE_NAME);

   /**
    * Returns the singleton gadget transfer instance.
    */
   public static GadgetTransfer getInstance() {
      return instance;
   }
   /**
    * Avoid explicit instantiation
    */
   private GadgetTransfer() {
   }
   protected Gadget[] fromByteArray(byte[] bytes) {
      DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes));

      try {
         /* read number of gadgets */
         int n = in.readInt();
         /* read gadgets */
         Gadget[] gadgets = new Gadget[n];
         for (int i = 0; i < n; i++) {
            Gadget gadget = readGadget(null, in);
            if (gadget == null) {
               return null;
            }
            gadgets[i] = gadget;
         }
         return gadgets;
      } catch (IOException e) {
         return null;
      }
   }
   /*
    * Method declared on Transfer.
    */
   protected int[] getTypeIds() {
      return new int[] { TYPEID };
   }
   /*
    * Method declared on Transfer.
    */
   protected String[] getTypeNames() {
      return new String[] { TYPE_NAME };
   }
   /*
    * Method declared on Transfer.
    */
   protected void javaToNative(Object object, TransferData transferData) {
      byte[] bytes = toByteArray((Gadget[])object);
      if (bytes != null)
         super.javaToNative(bytes, transferData);
   }
   /*
    * Method declared on Transfer.
    */
   protected Object nativeToJava(TransferData transferData) {
      byte[] bytes = (byte[])super.nativeToJava(transferData);
      return fromByteArray(bytes);
   }
   /**
    * Reads and returns a single gadget from the given stream.
    */
   private Gadget readGadget(Gadget parent, DataInputStream dataIn) throws IOException {
      /**
       * Gadget serialization format is as follows:
       * (String) name of gadget
       * (int) number of child gadgets
       * (Gadget) child 1
       * ... repeat for each child
       */
      String name = dataIn.readUTF();
      int n = dataIn.readInt();
      Gadget newParent = new Gadget(parent, name);
      for (int i = 0; i < n; i++) {
         readGadget(newParent, dataIn);
      }
      return newParent;
   }
   protected byte[] toByteArray(Gadget[] gadgets) {
      /**
       * Transfer data is an array of gadgets.  Serialized version is:
       * (int) number of gadgets
       * (Gadget) gadget 1
       * (Gadget) gadget 2
       * ... repeat for each subsequent gadget
       * see writeGadget for the (Gadget) format.
       */
      ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
      DataOutputStream out = new DataOutputStream(byteOut);

      byte[] bytes = null;

      try {
         /* write number of markers */
         out.writeInt(gadgets.length);

         /* write markers */
         for (int i = 0; i < gadgets.length; i++) {
            writeGadget((Gadget)gadgets[i], out);
         }
         out.close();
         bytes = byteOut.toByteArray();
      } catch (IOException e) {
         //when in doubt send nothing
      }
      return bytes;
   }
   /**
    * Writes the given gadget to the stream.
    */
   private void writeGadget(Gadget gadget, DataOutputStream dataOut) throws IOException {
      /**
       * Gadget serialization format is as follows:
       * (String) name of gadget
       * (int) number of child gadgets
       * (Gadget) child 1
       * ... repeat for each child
       */
      dataOut.writeUTF(gadget.getName());
      Gadget[] children = gadget.getChildren();
      dataOut.writeInt(children.length);
      for (int i = 0; i < children.length; i++) {
         writeGadget(children[i], dataOut);
      }
   }
}