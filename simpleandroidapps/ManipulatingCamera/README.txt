APPROACH
========
1. Used an intent specifically for using Camera (MediaStore.ACTION_IMAGE_CAPTURE)
2. Created an image file with JPEG when the activity is resolved using (getExternalStorageDirectory())
3. Formmatted the picture using (String.format())
4. Decoded the picture and set the background using (Bitmap.decode/BitmapDrawable)
5. Created a location manager that updates location and grabbed the lattitude and longitude of the last updated information
6. Used FileWriter, BufferedWriter to write into PicListXML file
7. Updated the count of the photo through SharedPreferences (used Editor.putInt() and SharePreferences.getInt())
8. Added items inside the ActionBar and also made the Buttons actual show AlertDialog with texts inside

FUNCTIONS
=========
takePhoto(View v): starts the camera activity and creates the image file when intent is resolved
createImageFile(): creates the jpeg file that the picture is stored in
onActivityResult(int requestcode, int resultcode, Intent data): sets the background, writes the information about the photo into xml, updates the photoCounter/SharedPreferences
onOptionsItemSelected(MenuItem item): used to call the functions when each of the buttons inside the actionbar are pressed
settingsAction(View v): special alert dialog pops up and displays "Haha, no settings!"
helpAction(View v): special alsert dialog pops up displaying my name, version # of app, and a random joke

COMPATIBILITY
=============
I used a Nexus 4 emulator inside GenyMotion. Any Phone with the latest android (Kit Kat 4.4.2) is suited for this app.
