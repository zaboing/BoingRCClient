   M S B A M L   `   `   `  ����E  AJSONEditor, Version=1.0.0.0, Culture=neutral, PublicKeyToken=null'clr-namespace:JSONEditor
JSONEditor  5   	       JSONEditor.MainWindow   5   	   \ XPresentationFramework, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35R NWindowsBase, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35W SPresentationCore, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35D 9http://schemas.microsoft.com/winfx/2006/xaml/presentation    5   	   R NSystem.Xaml, Version=4.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089:x,http://schemas.microsoft.com/winfx/2006/xaml     5   	   )convclr-namespace:JSONEditor     5   	   -   5        $	��Win��5   5     � Title$  
MainWindow��6	   $	��350��6   $	��525��6)    � 
ResizeMode$ NoResize=�6@    � 	Resources 5         JSONEditor.RemoveConverter   
RemoveConv��%   &	          5   
   6   5      .��5	      � .+�5
   
   &   System.Windows.Controls.DataGrid  -   5        $��CommandGrid��5
      $
��Left=�6&   $��	10,38,0,0q�6A   $	��Top=�6T   $	��219��6l   $	��240��6y      ColumnWidth5  /System.Windows.Controls.DataGridLengthConverter$ 1* 5   J   ��6   ��  �� ElementName$	 Win�� �� Path$ Commands�6[     5   
   -   5        $��	AliasGrid��5      $��Right=�6$   $��	0,38,10,0q�6@   $	��Top=�6S   $	��219��6k   $	��240��6x   $ 1* 5   I   ��6   �� $	 Win��$ Aliases�6Z   �� 5   
   -   5        $	��Add��5      $
��Left=�6   $��
10,262,0,0q�6:   $	��Top=�6N   $��75��6f   6q   �� 5   
   -   5        $��Remove��5      $
��Left=�6"   $��175,262,0,0q�6=   $	��Top=�6R   $��75��6j   {�5      �� $ CommandGrid��$ SelectedIndex� �� 	Converter# [  6z   �� 5   
   -   5        $	��Add��5      $��Right=�6   $��0,262,174,0q�6;   $	��Top=�6P   $��76��6h   6s   �� 5   
   -   5        $��Remove��5      $��Right=�6"   $��
0,262,10,0q�6>   $	��Top=�6R   $��75��6j   {�5      �� $ 	AliasGrid��$ SelectedIndex�# [  6x   �� 5   
   $
��Left=�6   $��23��6/   $��	10,15,0,0q�6;   $
��Wrap=�6N   $��Commands��6b   $	��Top=�6r   $	��120��6�   $
��Bold"�6�   �� 5   
   $��Right=�6   $��23��60   $��	0,15,10,0q�6<   $
��Wrap=�6O   $��Aliases��6c   $	��Top=�6r   $	��240��6�   $
��Bold"�6�   5      5      