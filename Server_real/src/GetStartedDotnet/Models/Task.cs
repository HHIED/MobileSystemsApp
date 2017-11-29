using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace GetStartedDotnet.Models
{
    public class Task
    {
        [Key]
        public int Id { get; set; }
        public string Description { get; set; }
        public byte[] Image { get; set; }
        public int Score { get; set; }
        public float Lattitude { get; set; }
        public float Longtitude { get; set; }
        public byte[] CompletedImage { get; set; }
        [ForeignKey("CreatorId")]
        public virtual User Creator { get; set; }
        [ForeignKey("AccepterId")]
        public virtual User Accepter { get; set; }
        public bool IsApproved { get; set; }


        public Task()
        {

        }

        public Task(string description, byte[] image, int score, float lattitude, float longtitude, User creator, User accepter)
        {
            Description = description;
            Image = image;
            Score = score;
            Lattitude = lattitude;
            Longtitude = longtitude;
            Creator = creator;
            Accepter = accepter;
        }

    }
}
