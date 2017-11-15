using System;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Infrastructure;
using Microsoft.EntityFrameworkCore.Metadata;
using Microsoft.EntityFrameworkCore.Migrations;
using GetStartedDotnet.Models;
using System.Collections.Generic;

namespace GetStartedDotnet.Migrations
{
    [DbContext(typeof(VisitorsDbContext))]
    partial class VisitorsDbContextModelSnapshot : ModelSnapshot
    {
        protected override void BuildModel(ModelBuilder modelBuilder)
        {
            modelBuilder
                .HasAnnotation("ProductVersion", "1.1.3");

            modelBuilder.Entity("GetStartedDotnet.Models.User", b =>
                {
                    b.Property<int>("Id");

                    b.HasKey("Id");

                    b.ToTable("Users");
                });
        }
    }
}
