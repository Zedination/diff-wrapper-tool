reg add "HKEY_CURRENT_USER\Software\Classes\Directory\shell\Diff Wrapper Tool" /ve /d "Open with Diff Wrapper Tool" /f
reg add "HKEY_CURRENT_USER\Software\Classes\Directory\shell\Diff Wrapper Tool" /v "Icon" /d "C:\Users\Admin\AppData\Local\Diff Wrapper\app.ico" /f
reg add "HKEY_CURRENT_USER\Software\Classes\Directory\shell\Diff Wrapper Tool\command" /ve /d "\"C:\Program Files\Diff Wrapper Tool\Diff Wrapper Tool.exe\" \"%V\"" /f
reg add "HKEY_CURRENT_USER\Software\Classes\Directory\Background\shell\Diff Wrapper Tool" /ve /d "Open with Diff Wrapper Tool" /f
reg add "HKEY_CURRENT_USER\Software\Classes\Directory\Background\shell\Diff Wrapper Tool" /v "Icon" /d "C:\Users\Admin\AppData\Local\Diff Wrapper\app.ico" /f
reg add "HKEY_CURRENT_USER\Software\Classes\Directory\Background\shell\Diff Wrapper Tool\command" /ve /d "\"C:\Program Files\Diff Wrapper Tool\Diff Wrapper Tool.exe\" \"%V\"" /f
