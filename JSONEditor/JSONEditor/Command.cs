﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace JSONEditor
{
    public class Command
    {
        public String Key { get; set; }
        public String Value { get; set; }

        public Command(String key, String value)
        {
            Key = key;
            Value = value;
        }
    }
}
