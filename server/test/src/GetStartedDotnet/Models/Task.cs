using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.ComponentModel.DataAnnotations;

namespace GetStartedDotnet.Models
{
    public class Task
    {
        [Key]
        public int Id { get; set; }
        public string Description { get; set; }
        public string Image { get; set; }
        public int Score { get; set; }
        public float Lattitude { get; set; }
        public float Longtitude { get; set; }


    }
}
